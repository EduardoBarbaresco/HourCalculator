package com.example.hourcalculator.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Registro de histórico de um cálculo concluído.
 */
data class CalculationRecord(
    val id: Long = System.currentTimeMillis(),
    val expression: String,
    val resultSexagesimal: String,
    val resultCentesimal: String,
    val mode: CalculatorMode,
    val timestamp: Long = System.currentTimeMillis()
) {
    val formattedTime: String
        get() {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
}
