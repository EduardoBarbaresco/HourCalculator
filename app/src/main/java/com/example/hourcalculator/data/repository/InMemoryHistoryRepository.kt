package com.example.hourcalculator.data.repository

import com.example.hourcalculator.domain.model.CalculationRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Implementação reativa e em memória do repositório de histórico.
 */
class InMemoryHistoryRepository : HistoryRepository {

    private val _history = MutableStateFlow<List<CalculationRecord>>(emptyList())
    private val historyFlow = _history.asStateFlow()

    override fun getHistory(): Flow<List<CalculationRecord>> = historyFlow

    override suspend fun addRecord(record: CalculationRecord) {
        val currentList = _history.value.toMutableList()
        currentList.add(0, record) // Adiciona no início da lista para visualização recente primeiro
        if (currentList.size > 100) {
            currentList.removeAt(currentList.lastIndex) // Limite de 100 registros
        }
        _history.value = currentList
    }

    override suspend fun clearHistory() {
        _history.value = emptyList()
    }
}
