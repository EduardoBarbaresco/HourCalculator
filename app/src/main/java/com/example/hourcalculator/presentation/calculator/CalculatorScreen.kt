package com.example.hourcalculator.presentation.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hourcalculator.presentation.calculator.components.DisplaySection
import com.example.hourcalculator.presentation.calculator.components.KeypadSection
import com.example.hourcalculator.presentation.calculator.components.ModeSelector
import com.example.hourcalculator.presentation.history.HistoryBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Calculadora de Horas",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(CalculatorUiEvent.OnToggleHistory(true)) }) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Ver Histórico",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            // Seletor de Modo (HH:MM vs Centesimal)
            ModeSelector(
                currentMode = uiState.mode,
                onModeSelected = { newMode ->
                    viewModel.onEvent(CalculatorUiEvent.OnToggleMode(newMode))
                }
            )

            // Visor Duplo com Badge Centesimal
            DisplaySection(
                expressionTape = uiState.expressionTape,
                primaryDisplay = uiState.primaryDisplay,
                secondaryDisplay = uiState.secondaryDisplay,
                operandContext = uiState.currentOperandContext,
                mode = uiState.mode,
                errorMessage = uiState.errorMessage
            )

            Spacer(modifier = Modifier.weight(1f))

            // Teclado da Calculadora
            KeypadSection(
                mode = uiState.mode,
                activeOperator = uiState.activeOperator,
                onEvent = { event -> viewModel.onEvent(event) }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Painel Deslizante de Histórico
        if (uiState.isHistoryOpen) {
            HistoryBottomSheet(
                history = uiState.history,
                onDismiss = { viewModel.onEvent(CalculatorUiEvent.OnToggleHistory(false)) },
                onClearHistory = { viewModel.onEvent(CalculatorUiEvent.OnClearHistory) },
                onSelectRecord = { record -> viewModel.onEvent(CalculatorUiEvent.OnSelectHistoryItem(record)) }
            )
        }
    }
}
