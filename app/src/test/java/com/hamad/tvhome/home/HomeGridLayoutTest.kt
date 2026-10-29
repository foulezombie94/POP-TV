package com.hamad.tvhome.home

import com.hamad.tvhome.GridNav
import com.hamad.tvhome.Dir
import org.junit.Assert.*
import org.junit.Test

class HomeGridLayoutTest {
    @Test fun everyFocusedIconAndItsLabelFitInsideTheScreen() {
        for (count in 1..65) for (index in 0 until count) {
            val scroll = HomeGridLayout.scrollFor(index)
            val top = HomeGridLayout.EXPANDED_TOP + index / GridNav.COLUMNS * HomeGridLayout.ROW_PITCH - scroll
            assertTrue("Clipped top at $index", top >= 30f)
            assertTrue("Clipped label at $index", top + 78f + 40f <= HomeGridLayout.VIEWPORT)
            assertTrue("Focused app wasn't composed", index in HomeGridLayout.visibleIndices(scroll, count))
        }
    }
    @Test fun sixColumnsAreReachableWithoutHorizontalWrapping() {
        assertEquals(6, GridNav.COLUMNS)
        for (col in 0..5) assertEquals(col + 6, GridNav.move(col, 18, Dir.Down))
        assertNull(GridNav.move(5, 18, Dir.Right))
        assertNull(GridNav.move(6, 18, Dir.Left))
    }
    @Test fun dockExpandsOnlyWhenItsOwnRowHasFocus() {
        assertEquals(0f, HomeGridLayout.scrollFor(0))
        assertEquals(0f, HomeGridLayout.scrollFor(5))
        assertTrue(HomeGridLayout.scrollFor(6) > 0f)
        assertTrue(HomeGridLayout.scrollFor(24) > HomeGridLayout.scrollFor(6))
    }
    @Test fun emptyCatalogComposesNoInvalidIndices() {
        assertTrue(HomeGridLayout.visibleIndices(0f, 0).isEmpty())
    }
}
