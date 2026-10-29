package com.hamad.tvhome.home

import com.hamad.tvhome.GridNav
import kotlin.math.max

/** tvOS Home: six columns, a dock, and a Top Shelf that withdraws below the dock. Units: dp. */
object HomeGridLayout {
    const val EXPANDED_TOP = 414f
    const val COLLAPSED_TOP = 54f
    const val ROW_PITCH = 130f
    const val VIEWPORT = 540f
    fun scrollFor(index: Int): Float {
        val row = index.coerceAtLeast(0) / GridNav.COLUMNS
        return if (row == 0) 0f else EXPANDED_TOP - COLLAPSED_TOP + max(0, row - 2) * ROW_PITCH
    }
    fun visibleIndices(scroll: Float, count: Int): IntRange {
        if (count <= 0) return IntRange.EMPTY
        val firstRow = max(0, ((scroll - EXPANDED_TOP) / ROW_PITCH).toInt() - 1)
        val lastRow = max(firstRow, ((scroll + VIEWPORT - EXPANDED_TOP) / ROW_PITCH).toInt() + 1)
        val first = (firstRow * GridNav.COLUMNS).coerceAtMost(count - 1)
        val last = ((lastRow + 1) * GridNav.COLUMNS - 1).coerceAtMost(count - 1)
        return first..last
    }
}
