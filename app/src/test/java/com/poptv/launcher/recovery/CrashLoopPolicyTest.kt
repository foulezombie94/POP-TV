package com.poptv.launcher.recovery
import org.junit.Assert.*
import org.junit.Test

class CrashLoopPolicyTest {
    @Test fun repeatedStartupCrashesRecover() {
        val first = CrashLoopPolicy.failures(0, 0, 1_000_000)
        val second = CrashLoopPolicy.failures(first, 1_000_000, 1_010_000)
        assertFalse(CrashLoopPolicy.shouldRecover(first))
        assertTrue(CrashLoopPolicy.shouldRecover(second))
    }
    @Test fun oldFailuresExpire() {
        assertEquals(1, CrashLoopPolicy.failures(1, 1000, 1000 + CrashLoopPolicy.WINDOW_MS + 1))
    }
    @Test fun clockCorrectionDoesNotCreateCrashLoop() {
        assertEquals(1, CrashLoopPolicy.failures(1, 10_000, 1000))
    }
}
