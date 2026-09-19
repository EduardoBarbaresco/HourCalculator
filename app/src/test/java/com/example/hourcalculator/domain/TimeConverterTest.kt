package com.example.hourcalculator.domain

import com.example.hourcalculator.domain.engine.TimeConverter
import com.example.hourcalculator.domain.model.TimeSpan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class TimeConverterTest {

    @Test
    fun testParseSexagesimal() {
        val time1 = TimeConverter.parseSexagesimal("00:30")
        assertNotNull(time1)
        assertEquals(30.0, time1!!.totalMinutes, 0.001)
        assertEquals("00:30", time1.formatSexagesimal())
        assertEquals("0,50 h", time1.formatCentesimal())

        val time2 = TimeConverter.parseSexagesimal("01:45")
        assertNotNull(time2)
        assertEquals(105.0, time2!!.totalMinutes, 0.001)
        assertEquals("01:45", time2.formatSexagesimal())
        assertEquals("1,75 h", time2.formatCentesimal())
    }

    @Test
    fun testParseDecimal() {
        val time1 = TimeConverter.parseDecimal("0.5")
        assertNotNull(time1)
        assertEquals(30.0, time1!!.totalMinutes, 0.001)
        assertEquals("00:30", time1.formatSexagesimal())

        val time2 = TimeConverter.parseDecimal("1,75")
        assertNotNull(time2)
        assertEquals(105.0, time2!!.totalMinutes, 0.001)
        assertEquals("01:45", time2.formatSexagesimal())

        val time3 = TimeConverter.parseDecimal("7.75")
        assertNotNull(time3)
        assertEquals(465.0, time3!!.totalMinutes, 0.001)
        assertEquals("07:45", time3.formatSexagesimal())
    }

    @Test
    fun testMinutesToCentesimal() {
        assertEquals(0.50, TimeConverter.minutesToCentesimal(30.0), 0.001)
        assertEquals(0.75, TimeConverter.minutesToCentesimal(45.0), 0.001)
        assertEquals(1.25, TimeConverter.minutesToCentesimal(75.0), 0.001)
        assertEquals(0.33, TimeConverter.minutesToCentesimal(20.0), 0.001)
    }

    @Test
    fun testNegativeTimeSpan() {
        val negative = TimeSpan(-45.0)
        assertEquals("-00:45", negative.formatSexagesimal())
        assertEquals("-0,75 h", negative.formatCentesimal())
    }
}
