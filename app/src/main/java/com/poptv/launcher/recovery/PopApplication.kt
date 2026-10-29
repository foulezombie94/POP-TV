package com.poptv.launcher.recovery

import android.app.Application
import android.os.Handler
import android.os.Looper

class PopApplication : Application() {
    private var launching = false
    private val main by lazy { Handler(Looper.getMainLooper()) }
    private val healthy = Runnable {
        launching = false
        getSharedPreferences("recovery", MODE_PRIVATE).edit().clear().apply()
    }
    override fun onCreate() {
        super.onCreate()
        val previous = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, error ->
            if (launching) runCatching {
                val p = getSharedPreferences("recovery", MODE_PRIVATE)
                val now = System.currentTimeMillis()
                val count = CrashLoopPolicy.failures(p.getInt("failures", 0), p.getLong("last", 0), now)
                p.edit().putInt("failures", count).putLong("last", now).commit()
                if (CrashLoopPolicy.shouldRecover(count) && StockHome.available(this)) StockHome.restore(this)
            }
            previous?.uncaughtException(thread, error)
        }
    }
    fun startingHome() {
        launching = true; main.removeCallbacks(healthy)
        main.postDelayed(healthy, CrashLoopPolicy.STABLE_MS)
    }
}
