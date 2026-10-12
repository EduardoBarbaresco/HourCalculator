package com.example.hourcalculator.presentation.calculator.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hourcalculator.ui.theme.ClearColor
import com.example.hourcalculator.ui.theme.EqualsColor
import com.example.hourcalculator.ui.theme.HourCalculatorTheme
import com.example.hourcalculator.ui.theme.OperatorColor
import com.example.hourcalculator.ui.theme.QuickAddContainerDark
import com.example.hourcalculator.ui.theme.QuickAddContainerLight
import com.example.hourcalculator.ui.theme.QuickAddOnContainerDark
import com.example.hourcalculator.ui.theme.QuickAddOnContainerLight

enum class ButtonStyleType {
    DIGIT,
    OPERATOR,
    EQUALS,
    CLEAR,
    ACTION,
    QUICK_ADD
}

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    type: ButtonStyleType = ButtonStyleType.DIGIT,
    isActive: Boolean = false,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val (containerColor, contentColor) = when (type) {
        ButtonStyleType.DIGIT -> Pair(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurface
        )
        ButtonStyleType.OPERATOR -> Pair(
            if (isActive) MaterialTheme.colorScheme.primary else OperatorColor,
            Color.White
        )
        ButtonStyleType.EQUALS -> Pair(EqualsColor, Color.White)
        ButtonStyleType.CLEAR -> Pair(ClearColor, Color.White)
        ButtonStyleType.ACTION -> Pair(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer
        )
        ButtonStyleType.QUICK_ADD -> Pair(
            if (isDark) QuickAddContainerDark else QuickAddContainerLight,
            if (isDark) QuickAddOnContainerDark else QuickAddOnContainerLight
        )
    }

    Button(
        onClick = onClick,
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = if (type == ButtonStyleType.QUICK_ADD) 15.sp else 22.sp,
            fontWeight = if (type == ButtonStyleType.DIGIT) FontWeight.Normal else FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, widthDp = 80, heightDp = 80)
@Composable
fun CalculatorButtonPreview() {
    HourCalculatorTheme {
        CalculatorButton(
            text = "7",
            onClick = {}
        )
    }
}
