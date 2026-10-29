package com.hamad.tvhome.tmdb

/** Sleeping timer, never a frame loop. Limits network, retained data and image work. */
object CarouselPolicy {
    const val COUNT = 5
    const val INTERVAL_MS = 7_000L
    const val CACHE_MS = 6 * 60 * 60 * 1000L
    const val RETRY_MS = 15 * 60 * 1000L
    fun fresh(now: Long, saved: Long) = saved > 0 && now >= saved && now - saved < CACHE_MS
    fun next(index: Int, count: Int) = if (count < 1) 0 else (index + 1).mod(count)
    fun active(resumed: Boolean, topRow: Boolean, profile: Boolean, editing: Boolean, sheet: Boolean) =
        resumed && topRow && !profile && !editing && !sheet
}
