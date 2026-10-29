package com.hamad.tvhome.recovery

object CrashLoopPolicy {
    const val WINDOW_MS = 120_000L
    const val STABLE_MS = 30_000L
    fun failures(previous: Int, lastFailureMs: Long, nowMs: Long): Int =
        if (nowMs >= lastFailureMs && nowMs - lastFailureMs <= WINDOW_MS) previous + 1 else 1
    fun shouldRecover(failures: Int) = failures >= 2
}
