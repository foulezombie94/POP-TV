package com.poptv.launcher

import android.view.KeyEvent
import org.junit.Assert.*
import org.junit.Test

class RemoteKeyPolicyTest {
    @Test fun mediaVolumePowerAndFreeboxKeysRemainSystemOwned() {
        for (key in listOf(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_MUTE, KeyEvent.KEYCODE_POWER,
            KeyEvent.KEYCODE_HOME, KeyEvent.KEYCODE_TV, KeyEvent.KEYCODE_GUIDE)) {
            assertTrue("System key $key must never become an app menu", RemoteKeyPolicy.belongsToSystem(key))
        }
    }
    @Test fun navigationAndContextMenuRemainAvailable() {
        for (key in listOf(KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN,
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_MENU)) {
            assertFalse(RemoteKeyPolicy.belongsToSystem(key))
        }
    }
}
