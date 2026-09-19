package com.example.hourcalculator.presentation

import com.example.hourcalculator.data.repository.InMemoryHistoryRepository
import com.example.hourcalculator.domain.model.CalculatorMode
import com.example.hourcalculator.domain.model.Operator
import com.example.hourcalculator.presentation.calculator.CalculatorUiEvent
import com.example.hourcalculator.presentation.calculator.CalculatorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CalculatorViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CalculatorViewModel
    private lateinit var repository: InMemoryHistoryRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = InMemoryHistoryRepository()
        viewModel = CalculatorViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSimpleAdditionFlow() = runTest {
        // Digita 30 (00:30)
        viewModel.onEvent(CalculatorUiEvent.OnDigitClick('3'))
        viewModel.onEvent(CalculatorUiEvent.OnDigitClick('0'))
        assertEquals("00:30", viewModel.uiState.value.primaryDisplay)
        assertEquals("0,50 h", viewModel.uiState.value.secondaryDisplay)

        // Clica no operador '+'
        viewModel.onEvent(CalculatorUiEvent.OnOperatorClick(Operator.ADD))
        assertEquals("00:30 +", viewModel.uiState.value.expressionTape)

        // Digita 30 (00:30)
        viewModel.onEvent(CalculatorUiEvent.OnDigitClick('3'))
        viewModel.onEvent(CalculatorUiEvent.OnDigitClick('0'))

        // Clica no igual '='
        viewModel.onEvent(CalculatorUiEvent.OnEqualsClick)
        advanceUntilIdle()

        // Resultado deve ser 01:00 (1,00 h)
        assertEquals("01:00", viewModel.uiState.value.primaryDisplay)
        assertEquals("1,00 h", viewModel.uiState.value.secondaryDisplay)
        assertEquals("00:30 + 00:30 =", viewModel.uiState.value.expressionTape)

        // Verifica se foi adicionado ao histórico
        assertEquals(1, viewModel.uiState.value.history.size)
        val historyRecord = viewModel.uiState.value.history.first()
        assertEquals("00:30 + 00:30", historyRecord.expression)
        assertEquals("01:00", historyRecord.resultSexagesimal)
        assertEquals("1,00 h", historyRecord.resultCentesimal)
    }

    @Test
    fun testQuickAddMinutes() = runTest {
        // Adiciona 15 minutos
        viewModel.onEvent(CalculatorUiEvent.OnQuickAddMinutes(15))
        assertEquals("00:15", viewModel.uiState.value.primaryDisplay)
        assertEquals("0,25 h", viewModel.uiState.value.secondaryDisplay)

        // Adiciona mais 30 minutos
        viewModel.onEvent(CalculatorUiEvent.OnQuickAddMinutes(30))
        assertEquals("00:45", viewModel.uiState.value.primaryDisplay)
        assertEquals("0,75 h", viewModel.uiState.value.secondaryDisplay)
    }

    @Test
    fun testSwitchModeToDecimal() = runTest {
        viewModel.onEvent(CalculatorUiEvent.OnToggleMode(CalculatorMode.DECIMAL_TO_SEXAGESIMAL))
        assertEquals(CalculatorMode.DECIMAL_TO_SEXAGESIMAL, viewModel.uiState.value.mode)

        // Digita 1,5
        viewModel.onEvent(CalculatorUiEvent.OnDigitClick('1'))
        viewModel.onEvent(CalculatorUiEvent.OnDecimalClick)
        viewModel.onEvent(CalculatorUiEvent.OnDigitClick('5'))

        assertEquals("1,5 h", viewModel.uiState.value.primaryDisplay)
        assertEquals("01:30", viewModel.uiState.value.secondaryDisplay)
    }
}
