package com.hamad.tvhome.system

import com.hamad.tvhome.Dir

/** Two-column profile picker. Moving down never becomes a sideways move in the last row. */
object ProfilePickerNav {
    fun move(index: Int, count: Int, dir: Dir): Int {
        if (count <= 0) return 0
        val i = index.coerceIn(0, count - 1)
        return when (dir) {
            Dir.Left -> if (i % 2 == 1) i - 1 else i
            Dir.Right -> if (i % 2 == 0 && i + 1 < count) i + 1 else i
            Dir.Up -> if (i >= 2) i - 2 else i
            Dir.Down -> if (i / 2 < (count - 1) / 2) (i + 2).coerceAtMost(count - 1) else i
        }
    }
}
