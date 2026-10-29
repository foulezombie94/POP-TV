package com.poptv.launcher.features.control

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

/** The same panel opens from the Home pill or a long HOME press, without grabbing volume keys. */
class ControlCenterActivity : ComponentActivity() {
    private val keys = KeySink()
    private lateinit var state: OverlayState
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state = OverlayState(keys) { finish() }
        val controls = SystemControls(this)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = state.close()
        })
        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                ControlCenter(state, controls, intent.getBooleanExtra("profiles", false)) { kind ->
                    controls.perform(kind)
                    state.close()
                }
            }
        }
    }
    override fun dispatchKeyEvent(event: KeyEvent): Boolean =
        if (::state.isInitialized && keys.dispatch(event)) true else super.dispatchKeyEvent(event)
}
