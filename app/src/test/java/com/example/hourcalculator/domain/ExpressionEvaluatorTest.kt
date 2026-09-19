package com.example.hourcalculator.domain

import com.example.hourcalculator.domain.engine.ExpressionEvaluator
import com.example.hourcalculator.domain.model.CalculatorMode
import com.example.hourcalculator.domain.model.Operator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ExpressionEvaluatorTest {

    @Test
    fun testAdditionSexagesimal() {
        // 00:30 (30 min) + 00:30 (30 min) = 01:00 (60 min / 1,00 h)
        val result = ExpressionEvaluator.evaluateBinary(
            leftMinutes = 30.0,
            operator = Operator.ADD,
            rightInput = "30",
            mode = CalculatorMode.SEXAGESIMAL_TO_DECIMAL
        )
        assertNotNull(result)
        assertEquals("01:00", result!!.displaySexagesimal)
        assertEquals("1,00 h", result.displayCentesimal)
    }

    @Test
    fun testSubtractionSexagesimal() {
        // 08:00 (480 min) - 01:45 (105 min) = 06:15 (375 min / 6,25 h)
        val result = ExpressionEvaluator.evaluateBinary(
            leftMinutes = 480.0,
            operator = Operator.SUBTRACT,
            rightInput = "145",
            mode = CalculatorMode.SEXAGESIMAL_TO_DECIMAL
        )
        assertNotNull(result)
        assertEquals("06:15", result!!.displaySexagesimal)
        assertEquals("6,25 h", result.displayCentesimal)
    }

    @Test
    fun testMultiplicationByScalar() {
        // 01:30 (90 min) * 2 = 03:00 (180 min / 3,00 h)
        val result = ExpressionEvaluator.evaluateBinary(
            leftMinutes = 90.0,
            operator = Operator.MULTIPLY,
            rightInput = "2",
            mode = CalculatorMode.SEXAGESIMAL_TO_DECIMAL
        )
        assertNotNull(result)
        assertEquals("03:00", result!!.displaySexagesimal)
        assertEquals("3,00 h", result.displayCentesimal)
    }

    @Test
    fun testDivisionByScalar() {
        // 05:00 (300 min) / 2 = 02:30 (150 min / 2,50 h)
        val result = ExpressionEvaluator.evaluateBinary(
            leftMinutes = 300.0,
            operator = Operator.DIVIDE,
            rightInput = "2",
            mode = CalculatorMode.SEXAGESIMAL_TO_DECIMAL
        )
        assertNotNull(result)
        assertEquals("02:30", result!!.displaySexagesimal)
        assertEquals("2,50 h", result.displayCentesimal)
    }

    @Test
    fun testDecimalModeAddition() {
        // 1.50h (90 min) + 0.75h (45 min) = 2.25h (135 min / 02:15)
        val result = ExpressionEvaluator.evaluateBinary(
            leftMinutes = 90.0,
            operator = Operator.ADD,
            rightInput = "0,75",
            mode = CalculatorMode.DECIMAL_TO_SEXAGESIMAL
        )
        assertNotNull(result)
        assertEquals("02:15", result!!.displaySexagesimal)
        assertEquals("2,25 h", result.displayCentesimal)
    }
}
