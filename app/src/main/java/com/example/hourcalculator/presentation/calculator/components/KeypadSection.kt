package com.example.hourcalculator.presentation.calculator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hourcalculator.domain.model.CalculatorMode
import com.example.hourcalculator.domain.model.Operator
import com.example.hourcalculator.presentation.calculator.CalculatorUiEvent

@Composable
fun KeypadSection(
    mode: CalculatorMode,
    activeOperator: Operator?,
    onEvent: (CalculatorUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Linha 1: Limpar, Backspace, Divisão, Multiplicação
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "C",
                type = ButtonStyleType.CLEAR,
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnClearClick) }
            )
            CalculatorButton(
                text = "⌫",
                type = ButtonStyleType.ACTION,
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnBackspaceClick) }
            )
            CalculatorButton(
                text = "÷",
                type = ButtonStyleType.OPERATOR,
                isActive = activeOperator == Operator.DIVIDE,
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnOperatorClick(Operator.DIVIDE)) }
            )
            CalculatorButton(
                text = "×",
                type = ButtonStyleType.OPERATOR,
                isActive = activeOperator == Operator.MULTIPLY,
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnOperatorClick(Operator.MULTIPLY)) }
            )
        }

        // Linha 2: 7, 8, 9, Subtração
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "7",
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnDigitClick('7')) }
            )
            CalculatorButton(
                text = "8",
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnDigitClick('8')) }
            )
            CalculatorButton(
                text = "9",
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnDigitClick('9')) }
            )
            CalculatorButton(
                text = "-",
                type = ButtonStyleType.OPERATOR,
                isActive = activeOperator == Operator.SUBTRACT,
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnOperatorClick(Operator.SUBTRACT)) }
            )
        }

        // Linha 3: 4, 5, 6, Adição
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "4",
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnDigitClick('4')) }
            )
            CalculatorButton(
                text = "5",
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnDigitClick('5')) }
            )
            CalculatorButton(
                text = "6",
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnDigitClick('6')) }
            )
            CalculatorButton(
                text = "+",
                type = ButtonStyleType.OPERATOR,
                isActive = activeOperator == Operator.ADD,
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnOperatorClick(Operator.ADD)) }
            )
        }

        // Linha 4: 1, 2, 3, Igual
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "1",
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnDigitClick('1')) }
            )
            CalculatorButton(
                text = "2",
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnDigitClick('2')) }
            )
            CalculatorButton(
                text = "3",
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnDigitClick('3')) }
            )
            CalculatorButton(
                text = "=",
                type = ButtonStyleType.EQUALS,
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnEqualsClick) }
            )
        }

        // Linha 5: Atalhos rápidos e zeros
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "0",
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnDigitClick('0')) }
            )
            CalculatorButton(
                text = if (mode == CalculatorMode.DECIMAL_TO_SEXAGESIMAL) "," else "00",
                type = if (mode == CalculatorMode.DECIMAL_TO_SEXAGESIMAL) ButtonStyleType.ACTION else ButtonStyleType.DIGIT,
                modifier = Modifier.weight(1f),
                onClick = {
                    if (mode == CalculatorMode.DECIMAL_TO_SEXAGESIMAL) {
                        onEvent(CalculatorUiEvent.OnDecimalClick)
                    } else {
                        onEvent(CalculatorUiEvent.OnDigitClick('0'))
                        onEvent(CalculatorUiEvent.OnDigitClick('0'))
                    }
                }
            )
            CalculatorButton(
                text = "+15m",
                type = ButtonStyleType.QUICK_ADD,
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnQuickAddMinutes(15)) }
            )
            CalculatorButton(
                text = "+30m",
                type = ButtonStyleType.QUICK_ADD,
                modifier = Modifier.weight(1f),
                onClick = { onEvent(CalculatorUiEvent.OnQuickAddMinutes(30)) }
            )
        }
    }
}
