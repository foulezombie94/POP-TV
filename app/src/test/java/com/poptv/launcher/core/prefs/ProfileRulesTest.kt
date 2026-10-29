package com.poptv.launcher.core.prefs

import org.junit.Assert.*
import org.junit.Test

class ProfileRulesTest {
    private val main = ProfileState(listOf(LocalProfile("main", "Principal")), "main")
    @Test fun selectingUnknownProfileDoesNotLoseCurrentProfile() {
        assertEquals(main, ProfileRules.select(main, "missing"))
    }
    @Test fun addingProfileCreatesAndActivatesARealDistinctProfile() {
        val added = ProfileRules.add(main, "second")
        assertEquals(2, added.profiles.size)
        assertEquals("second", added.active.id)
        assertEquals("main", ProfileRules.select(added, "main").active.id)
    }
    @Test fun profileLimitAndDuplicateIdsAreEnforced() {
        var state = main
        for (id in 1..10) state = ProfileRules.add(state, "p$id")
        assertEquals(6, state.profiles.size)
        assertEquals(main, ProfileRules.add(main, "main"))
    }
}
