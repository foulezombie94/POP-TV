package com.poptv.launcher.data.tmdb

import org.junit.Assert.*
import org.junit.Test

class CarouselPolicyTest {
    @Test fun cacheExpiresAfterSixHoursAndRejectsClockRollback() {
        assertTrue(CarouselPolicy.fresh(1_000, 999))
        assertFalse(CarouselPolicy.fresh(CarouselPolicy.CACHE_MS + 1, 1))
        assertFalse(CarouselPolicy.fresh(500, 999))
        assertFalse(CarouselPolicy.fresh(1000, 0))
    }
    @Test fun rotationWrapsAndHandlesEmptyLists() {
        assertEquals(0, CarouselPolicy.next(4, 5))
        assertEquals(1, CarouselPolicy.next(0, 5))
        assertEquals(0, CarouselPolicy.next(0, 0))
        assertEquals(7_000L, CarouselPolicy.INTERVAL_MS)
    }
    @Test fun pausesForEveryObscuredState() {
        assertTrue(CarouselPolicy.active(true, true, false, false, false))
        assertFalse(CarouselPolicy.active(false, true, false, false, false))
        assertFalse(CarouselPolicy.active(true, false, false, false, false))
        assertFalse(CarouselPolicy.active(true, true, true, false, false))
        assertFalse(CarouselPolicy.active(true, true, false, true, false))
        assertFalse(CarouselPolicy.active(true, true, false, false, true))
    }
}
