package com.example.hourcalculator.presentation.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hourcalculator.data.repository.HistoryRepository
import com.example.hourcalculator.data.repository.InMemoryHistoryRepository
import com.example.hourcalculator.domain.engine.ExpressionEvaluator
import com.example.hourcalculator.domain.engine.TimeConverter
import com.example.hourcalculator.domain.engine.TimeInputMask
import com.example.hourcalculator.domain.model.CalculationRecord
import com.example.hourcalculator.domain.model.CalculatorMode
import com.example.hourcalculator.domain.model.Operator
import com.example.hourcalculator.domain.model.TimeSpan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalculatorViewModel(
    private val historyRepository: HistoryRepository = InMemoryHistoryRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    // Estado interno da avaliação
    private var accumulatedMinutes: Double? = null
    private var pendingOperator: Operator? = null
    private var currentInputDigits: String = ""

    init {
        viewModelScope.launch {
            historyRepository.getHistory().collect { historyList ->
                _uiState.update { it.copy(history = historyList) }
            }
        }
        updateDisplayForCurrentState()
    }

    fun onEvent(event: CalculatorUiEvent) {
        when (event) {
            is CalculatorUiEvent.OnDigitClick -> handleDigit(event.digit)
            is CalculatorUiEvent.OnOperatorClick -> handleOperator(event.operator)
            is CalculatorUiEvent.OnDecimalClick -> handleDecimal()
            is CalculatorUiEvent.OnQuickAddMinutes -> handleQuickAdd(event.minutes)
            is CalculatorUiEvent.OnEqualsClick -> handleEquals()
            is CalculatorUiEvent.OnClearClick -> handleClear()
            is CalculatorUiEvent.OnBackspaceClick -> handleBackspace()
            is CalculatorUiEvent.OnToggleMode -> handleToggleMode(event.mode)
            is CalculatorUiEvent.OnToggleHistory -> _uiState.update { it.copy(isHistoryOpen = event.isOpen) }
            is CalculatorUiEvent.OnClearHistory -> handleClearHistory()
            is CalculatorUiEvent.OnSelectHistoryItem -> handleSelectHistoryItem(event.record)
        }
    }

    private fun handleDigit(digit: Char) {
        if (_uiState.value.isResultState) {
            // Se acabou de calcular e começou a digitar um número novo, reinicia
            accumulatedMinutes = null
            pendingOperator = null
            currentInputDigits = ""
            _uiState.update { it.copy(expressionTape = "", isResultState = false) }
        }

        currentInputDigits += digit
        updateDisplayForCurrentState()
    }

    private fun handleDecimal() {
        if (_uiState.value.mode == CalculatorMode.DECIMAL_TO_SEXAGESIMAL) {
            if (!currentInputDigits.contains(".") && !currentInputDigits.contains(",")) {
                if (currentInputDigits.isEmpty()) {
                    currentInputDigits = "0,"
                } else {
                    currentInputDigits += ","
                }
                updateDisplayForCurrentState()
            }
        }
    }

    private fun handleQuickAdd(minutesToAdd: Long) {
        val currentMinutes = if (currentInputDigits.isNotEmpty()) {
            if (_uiState.value.mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL) {
                TimeInputMask.rawDigitsToMinutes(currentInputDigits)
            } else {
                (currentInputDigits.replace(',', '.').toDoubleOrNull() ?: 0.0) * 60.0
            }
        } else {
            0.0
        }

        val newTotalMinutes = currentMinutes + minutesToAdd
        if (_uiState.value.mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL) {
            val hours = (newTotalMinutes / 60).toLong()
            val mins = (newTotalMinutes % 60).toLong()
            currentInputDigits = String.format(java.util.Locale.US, "%d%02d", hours, mins)
        } else {
            val decimalHours = newTotalMinutes / 60.0
            currentInputDigits = String.format(java.util.Locale.GERMANY, "%.2f", decimalHours)
        }

        _uiState.update { it.copy(isResultState = false) }
        updateDisplayForCurrentState()
    }

    private fun handleOperator(operator: Operator) {
        val mode = _uiState.value.mode

        // Se houver dígito digitado atualmente, avalia
        if (currentInputDigits.isNotEmpty()) {
            val currentOperandMinutes = if (mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL) {
                TimeInputMask.rawDigitsToMinutes(currentInputDigits)
            } else {
                (currentInputDigits.replace(',', '.').toDoubleOrNull() ?: 0.0) * 60.0
            }

            if (accumulatedMinutes == null) {
                accumulatedMinutes = currentOperandMinutes
            } else if (pendingOperator != null) {
                val eval = ExpressionEvaluator.evaluateBinary(
                    leftMinutes = accumulatedMinutes!!,
                    operator = pendingOperator!!,
                    rightInput = currentInputDigits,
                    mode = mode
                )
                if (eval != null) {
                    accumulatedMinutes = eval.timeSpan.totalMinutes
                }
            }
        }

        pendingOperator = operator
        val formattedAccumulated = if (accumulatedMinutes != null) {
            val span = TimeSpan(accumulatedMinutes!!)
            if (mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL) span.formatSexagesimal() else span.formatCentesimal()
        } else {
            "00:00"
        }

        _uiState.update {
            it.copy(
                expressionTape = "$formattedAccumulated ${operator.displaySymbol}",
                activeOperator = operator,
                isResultState = false
            )
        }

        currentInputDigits = ""
        updateDisplayForCurrentState()
    }

    private fun handleEquals() {
        val mode = _uiState.value.mode
        val operator = pendingOperator
        val left = accumulatedMinutes

        if (operator != null && left != null && currentInputDigits.isNotEmpty()) {
            val eval = ExpressionEvaluator.evaluateBinary(
                leftMinutes = left,
                operator = operator,
                rightInput = currentInputDigits,
                mode = mode
            )

            if (eval != null) {
                val rightDisplay = if (mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL) {
                    TimeInputMask.formatSexagesimalDigits(currentInputDigits)
                } else {
                    currentInputDigits
                }

                val leftSpan = TimeSpan(left)
                val leftDisplay = if (mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL) leftSpan.formatSexagesimal() else leftSpan.formatCentesimal()
                val fullExpression = "$leftDisplay ${operator.displaySymbol} $rightDisplay"

                val resultSexagesimal = eval.displaySexagesimal
                val resultCentesimal = eval.displayCentesimal

                val record = CalculationRecord(
                    expression = fullExpression,
                    resultSexagesimal = resultSexagesimal,
                    resultCentesimal = resultCentesimal,
                    mode = mode
                )

                viewModelScope.launch {
                    historyRepository.addRecord(record)
                }

                accumulatedMinutes = eval.timeSpan.totalMinutes
                pendingOperator = null
                currentInputDigits = ""

                val primary = if (mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL) resultSexagesimal else resultCentesimal
                val secondary = if (mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL) resultCentesimal else resultSexagesimal

                _uiState.update {
                    it.copy(
                        expressionTape = "$fullExpression =",
                        primaryDisplay = primary,
                        secondaryDisplay = secondary,
                        currentOperandContext = "",
                        activeOperator = null,
                        isResultState = true,
                        errorMessage = null
                    )
                }
            } else {
                _uiState.update { it.copy(errorMessage = "Operação inválida") }
            }
        }
    }

    private fun handleClear() {
        accumulatedMinutes = null
        pendingOperator = null
        currentInputDigits = ""
        _uiState.update {
            it.copy(
                expressionTape = "",
                primaryDisplay = if (it.mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL) "00:00" else "0,00 h",
                secondaryDisplay = if (it.mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL) "0,00 h" else "00:00",
                currentOperandContext = "",
                activeOperator = null,
                isResultState = false,
                errorMessage = null
            )
        }
    }

    private fun handleBackspace() {
        if (currentInputDigits.isNotEmpty()) {
            currentInputDigits = currentInputDigits.dropLast(1)
            updateDisplayForCurrentState()
        }
    }

    private fun handleToggleMode(newMode: CalculatorMode) {
        if (_uiState.value.mode != newMode) {
            handleClear()
            _uiState.update { it.copy(mode = newMode) }
            updateDisplayForCurrentState()
        }
    }

    private fun handleClearHistory() {
        viewModelScope.launch {
            historyRepository.clearHistory()
        }
    }

    private fun handleSelectHistoryItem(record: CalculationRecord) {
        val targetMinutes = TimeConverter.parseSexagesimal(record.resultSexagesimal)?.totalMinutes ?: 0.0
        accumulatedMinutes = targetMinutes
        pendingOperator = null
        currentInputDigits = ""

        val mode = _uiState.value.mode
        val span = TimeSpan(targetMinutes)
        val primary = if (mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL) span.formatSexagesimal() else span.formatCentesimal()
        val secondary = if (mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL) span.formatCentesimal() else span.formatSexagesimal()

        _uiState.update {
            it.copy(
                expressionTape = record.expression,
                primaryDisplay = primary,
                secondaryDisplay = secondary,
                isResultState = true,
                isHistoryOpen = false
            )
        }
    }

    private fun updateDisplayForCurrentState() {
        val mode = _uiState.value.mode

        if (mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL) {
            val formatted = TimeInputMask.formatSexagesimalDigits(currentInputDigits)
            val minutes = TimeInputMask.rawDigitsToMinutes(currentInputDigits)
            val span = TimeSpan(minutes)

            val primary = if (_uiState.value.isResultState && currentInputDigits.isEmpty() && accumulatedMinutes != null) {
                TimeSpan(accumulatedMinutes!!).formatSexagesimal()
            } else {
                formatted
            }

            val secondary = if (_uiState.value.isResultState && currentInputDigits.isEmpty() && accumulatedMinutes != null) {
                TimeSpan(accumulatedMinutes!!).formatCentesimal()
            } else {
                span.formatCentesimal()
            }

            val context = if (currentInputDigits.isNotEmpty()) "( ${span.formatCentesimal()} )" else ""

            _uiState.update {
                it.copy(
                    primaryDisplay = primary,
                    secondaryDisplay = secondary,
                    currentOperandContext = context,
                    errorMessage = null
                )
            }
        } else {
            // Decimal Mode
            val formatted = if (currentInputDigits.isEmpty()) "0,00" else currentInputDigits
            val decimalVal = currentInputDigits.replace(',', '.').toDoubleOrNull() ?: 0.0
            val span = TimeSpan.fromCentesimalHours(decimalVal)

            val primary = if (_uiState.value.isResultState && currentInputDigits.isEmpty() && accumulatedMinutes != null) {
                TimeSpan(accumulatedMinutes!!).formatCentesimal()
            } else {
                "$formatted h"
            }

            val secondary = if (_uiState.value.isResultState && currentInputDigits.isEmpty() && accumulatedMinutes != null) {
                TimeSpan(accumulatedMinutes!!).formatSexagesimal()
            } else {
                span.formatSexagesimal()
            }

            val context = if (currentInputDigits.isNotEmpty()) "( ${span.formatSexagesimal()} )" else ""

            _uiState.update {
                it.copy(
                    primaryDisplay = primary,
                    secondaryDisplay = secondary,
                    currentOperandContext = context,
                    errorMessage = null
                )
            }
        }
    }
}
