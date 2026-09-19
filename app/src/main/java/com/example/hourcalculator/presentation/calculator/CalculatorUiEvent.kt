package com.example.hourcalculator.presentation.calculator

import com.example.hourcalculator.domain.model.CalculationRecord
import com.example.hourcalculator.domain.model.CalculatorMode
import com.example.hourcalculator.domain.model.Operator

/**
 * Eventos da interface do usuário (MVI / UDF).
 */
sealed interface CalculatorUiEvent {
    data class OnDigitClick(val digit: Char) : CalculatorUiEvent
    data class OnOperatorClick(val operator: Operator) : CalculatorUiEvent
    data object OnDecimalClick : CalculatorUiEvent
    data class OnQuickAddMinutes(val minutes: Long) : CalculatorUiEvent
    data object OnEqualsClick : CalculatorUiEvent
    data object OnClearClick : CalculatorUiEvent
    data object OnBackspaceClick : CalculatorUiEvent
    data class OnToggleMode(val mode: CalculatorMode) : CalculatorUiEvent
    data class OnToggleHistory(val isOpen: Boolean) : CalculatorUiEvent
    data object OnClearHistory : CalculatorUiEvent
    data class OnSelectHistoryItem(val record: CalculationRecord) : CalculatorUiEvent
}
