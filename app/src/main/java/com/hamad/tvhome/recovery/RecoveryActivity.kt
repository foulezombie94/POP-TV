package com.hamad.tvhome.recovery

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

/** Independent of Compose, the catalog and network, so a UI failure cannot hide recovery. */
class RecoveryActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getBooleanExtra("enable", false)) {
            StockHome.enable(this)
            getSharedPreferences("recovery", MODE_PRIVATE).edit().clear().commit()
            startActivity(Intent().setClassName(packageName, "com.hamad.tvhome.MainActivity"))
            finish()
            return
        }
        val body = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(80, 60, 80, 60)
            setBackgroundColor(Color.rgb(18, 18, 20))
        }
        body.addView(TextView(this).apply {
            text = "Retour à l’accueil Freebox"; textSize = 30f; setTextColor(Color.WHITE)
        })
        body.addView(TextView(this).apply {
            text = "Free TV et vos applications sont conservés. Le retour désactive uniquement ce nouvel accueil et ses contrôles."
            textSize = 19f; setTextColor(Color.LTGRAY); setPadding(0, 24, 0, 32)
        })
        val restore = Button(this).apply {
            text = "Revenir à l’accueil Free d’origine"
            isEnabled = StockHome.available(this@RecoveryActivity)
            setOnClickListener { runCatching { StockHome.restore(this@RecoveryActivity); finish() }
                .onFailure { text = "Échec : utilisez retour-free.ps1 sur le PC" } }
        }
        body.addView(restore)
        body.addView(Button(this).apply {
            text = "Ouvrir les paramètres Android"
            setOnClickListener { runCatching { startActivity(Intent(android.provider.Settings.ACTION_SETTINGS)) } }
        })
        body.addView(Button(this).apply {
            text = "Réactiver et essayer Pop TV"
            setOnClickListener {
                StockHome.enable(this@RecoveryActivity)
                getSharedPreferences("recovery", MODE_PRIVATE).edit().clear().commit()
                startActivity(Intent().setClassName(packageName, "com.hamad.tvhome.MainActivity")); finish()
            }
        })
        setContentView(body)
        restore.requestFocus()
    }
}
