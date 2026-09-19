package com.example.hourcalculator.domain

import com.example.hourcalculator.domain.engine.TimeInputMask
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeInputMaskTest {

    @Test
    fun testFormatSexagesimalDigits() {
        assertEquals("00:00", TimeInputMask.formatSexagesimalDigits(""))
        assertEquals("00:03", TimeInputMask.formatSexagesimalDigits("3"))
        assertEquals("00:30", TimeInputMask.formatSexagesimalDigits("30"))
        assertEquals("01:30", TimeInputMask.formatSexagesimalDigits("130"))
        assertEquals("12:30", TimeInputMask.formatSexagesimalDigits("1230"))
        assertEquals("120:30", TimeInputMask.formatSexagesimalDigits("12030"))
    }

    @Test
    fun testRawDigitsToMinutes() {
        assertEquals(30.0, TimeInputMask.rawDigitsToMinutes("30"), 0.001)
        assertEquals(90.0, TimeInputMask.rawDigitsToMinutes("130"), 0.001) // 1h 30m = 90 min
        assertEquals(480.0, TimeInputMask.rawDigitsToMinutes("800"), 0.001) // 8h 00m = 480 min
    }
}
