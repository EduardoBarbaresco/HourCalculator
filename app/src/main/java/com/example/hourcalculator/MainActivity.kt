package com.example.hourcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.hourcalculator.presentation.calculator.CalculatorScreen
import com.example.hourcalculator.ui.theme.HourCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HourCalculatorTheme {
                CalculatorScreen()
            }
        }
    }
}
