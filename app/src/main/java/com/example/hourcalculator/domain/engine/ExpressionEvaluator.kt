package com.example.hourcalculator.domain.engine

import com.example.hourcalculator.domain.model.CalculatorMode
import com.example.hourcalculator.domain.model.Operator
import com.example.hourcalculator.domain.model.TimeSpan

/**
 * Motor de avaliação e cálculo de expressões matemáticas temporais.
 */
object ExpressionEvaluator {

    data class EvaluationResult(
        val timeSpan: TimeSpan,
        val isScalar: Boolean = false,
        val scalarValue: Double = 0.0
    ) {
        val displaySexagesimal: String
            get() = if (isScalar) String.format(java.util.Locale.US, "%.2f", scalarValue) else timeSpan.formatSexagesimal()

        val displayCentesimal: String
            get() = if (isScalar) String.format(java.util.Locale.GERMANY, "%.2f", scalarValue) else timeSpan.formatCentesimal()
    }

    /**
     * Avalia uma operação binária simples: [left] [operator] [right].
     */
    fun evaluateBinary(
        leftMinutes: Double,
        operator: Operator,
        rightInput: String,
        mode: CalculatorMode
    ): EvaluationResult? {
        val left = TimeSpan(leftMinutes)

        return when (operator) {
            Operator.ADD -> {
                val right = parseOperandToTimeSpan(rightInput, mode) ?: return null
                EvaluationResult(left + right)
            }
            Operator.SUBTRACT -> {
                val right = parseOperandToTimeSpan(rightInput, mode) ?: return null
                EvaluationResult(left - right)
            }
            Operator.MULTIPLY -> {
                // Multiplicação de Tempo por Escalar (ex: 01:30 * 2 = 03:00)
                val scalar = parseOperandToScalar(rightInput) ?: return null
                EvaluationResult(left * scalar)
            }
            Operator.DIVIDE -> {
                // Pode ser divisão por Escalar (ex: 05:00 / 2 = 02:30) ou Divisão Tempo / Tempo (ex: 02:00 / 00:30 = 4)
                val rightAsTime = parseOperandToTimeSpan(rightInput, mode)
                val scalar = parseOperandToScalar(rightInput)

                if (rightAsTime != null && rightInput.contains(":")) {
                    if (rightAsTime.totalMinutes == 0.0) return null
                    val ratio = left / rightAsTime
                    EvaluationResult(TimeSpan.ZERO, isScalar = true, scalarValue = ratio)
                } else if (scalar != null) {
                    if (scalar == 0.0) return null
                    EvaluationResult(left / scalar)
                } else {
                    null
                }
            }
        }
    }

    /**
     * Converte o operando atual para TimeSpan com base no modo ativo.
     */
    fun parseOperandToTimeSpan(operand: String, mode: CalculatorMode): TimeSpan? {
        return when (mode) {
            CalculatorMode.SEXAGESIMAL_TO_DECIMAL -> {
                if (operand.contains(":")) {
                    TimeConverter.parseSexagesimal(operand)
                } else {
                    val minutes = TimeInputMask.rawDigitsToMinutes(operand)
                    TimeSpan(minutes)
                }
            }
            CalculatorMode.DECIMAL_TO_SEXAGESIMAL -> {
                TimeConverter.parseDecimal(operand)
            }
        }
    }

    /**
     * Converte o operando para um valor escalar numérico.
     */
    fun parseOperandToScalar(operand: String): Double? {
        val clean = operand.trim().replace(',', '.')
        return clean.toDoubleOrNull()
    }
}
