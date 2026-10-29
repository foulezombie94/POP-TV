package com.poptv.launcher.system

import com.poptv.launcher.Dir
import org.junit.Assert.*
import org.junit.Test

class ProfilePickerNavTest {
    @Test fun downCannotSelectAddOnTheSameRow() {
        assertEquals(0, ProfilePickerNav.move(0, 2, Dir.Down))
        assertEquals(1, ProfilePickerNav.move(0, 2, Dir.Right))
    }
    @Test fun downReachesAShortFinalRowWithoutWrappingSideways() {
        assertEquals(2, ProfilePickerNav.move(1, 3, Dir.Down))
        assertEquals(2, ProfilePickerNav.move(2, 3, Dir.Right))
        assertEquals(2, ProfilePickerNav.move(2, 3, Dir.Down))
    }
    @Test fun upAndDownKeepTheColumnInCompleteRows() {
        assertEquals(3, ProfilePickerNav.move(1, 6, Dir.Down))
        assertEquals(1, ProfilePickerNav.move(3, 6, Dir.Up))
    }
}
