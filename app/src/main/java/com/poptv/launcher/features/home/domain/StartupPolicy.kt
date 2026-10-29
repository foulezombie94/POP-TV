package com.poptv.launcher.features.home.domain

object StartupPolicy {
    // Never leave an unresponsive launch window if the package manager fails.
    const val MAX_WAIT_MS = 1_500L
    fun mayDraw(ready: Boolean, elapsed: Long) = ready || elapsed >= MAX_WAIT_MS
}
