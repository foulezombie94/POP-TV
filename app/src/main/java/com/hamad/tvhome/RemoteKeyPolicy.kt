package com.hamad.tvhome

import android.view.KeyEvent

/** Playback, volume and power retain their Android/Freebox meaning on the home screen. */
object RemoteKeyPolicy {
    fun belongsToSystem(code: Int): Boolean = code in SYSTEM_KEYS
    private val SYSTEM_KEYS = setOf(
        KeyEvent.KEYCODE_HOME, KeyEvent.KEYCODE_POWER, KeyEvent.KEYCODE_SLEEP,
        KeyEvent.KEYCODE_VOLUME_UP, KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_MUTE,
        KeyEvent.KEYCODE_MUTE, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, KeyEvent.KEYCODE_MEDIA_PLAY,
        KeyEvent.KEYCODE_MEDIA_PAUSE, KeyEvent.KEYCODE_MEDIA_STOP, KeyEvent.KEYCODE_MEDIA_NEXT,
        KeyEvent.KEYCODE_MEDIA_PREVIOUS, KeyEvent.KEYCODE_MEDIA_REWIND, KeyEvent.KEYCODE_MEDIA_FAST_FORWARD,
        KeyEvent.KEYCODE_TV, KeyEvent.KEYCODE_GUIDE, KeyEvent.KEYCODE_SETTINGS,
    )
}
