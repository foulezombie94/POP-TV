package com.poptv.launcher.features.home.domain

import org.junit.Assert.*
import org.junit.Test

class StartupPolicyTest {
    @Test fun cachedAppsAllowImmediateFirstFrame() { assertTrue(StartupPolicy.mayDraw(true, 0)) }
    @Test fun heroCannotAppearBeforeApps() { assertFalse(StartupPolicy.mayDraw(false, 500)) }
    @Test fun failedPackageManagerCannotBlockWindowForever() {
        assertFalse(StartupPolicy.mayDraw(false, StartupPolicy.MAX_WAIT_MS - 1))
        assertTrue(StartupPolicy.mayDraw(false, StartupPolicy.MAX_WAIT_MS))
    }
}
