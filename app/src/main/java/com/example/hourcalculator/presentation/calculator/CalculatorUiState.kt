package com.example.hourcalculator.presentation.calculator

import com.example.hourcalculator.domain.model.CalculationRecord
import com.example.hourcalculator.domain.model.CalculatorMode
import com.example.hourcalculator.domain.model.Operator

/**
 * Estado imutável da interface do usuário da calculadora.
 */
data class CalculatorUiState(
    val mode: CalculatorMode = CalculatorMode.SEXAGESIMAL_TO_DECIMAL,
    val expressionTape: String = "",
    val currentInputRaw: String = "",
    val primaryDisplay: String = "00:00",
    val secondaryDisplay: String = "0,00 h",
    val currentOperandContext: String = "",
    val activeOperator: Operator? = null,
    val isResultState: Boolean = false,
    val history: List<CalculationRecord> = emptyList(),
    val isHistoryOpen: Boolean = false,
    val errorMessage: String? = null
)
