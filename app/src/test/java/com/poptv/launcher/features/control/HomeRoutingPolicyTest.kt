package com.poptv.launcher.features.control

import org.junit.Assert.*
import org.junit.Test

class HomeRoutingPolicyTest {
    @Test fun routesOnlyTheOriginalHomeWhileOptedIn() {
        assertTrue(HomeRoutingPolicy.redirectStockHome("net.oqee.launcher", true, true))
        assertFalse(HomeRoutingPolicy.redirectStockHome("net.oqee.androidtv", true, true))
        assertFalse(HomeRoutingPolicy.redirectStockHome("com.android.tv.settings", true, true))
    }
    @Test fun recoveryAndUserChoiceAlwaysWin() {
        assertFalse(HomeRoutingPolicy.redirectStockHome("net.oqee.launcher", true, false))
        assertFalse(HomeRoutingPolicy.redirectStockHome("net.oqee.launcher", false, true))
    }
}
