package com.example.hourcalculator.data.repository

import com.example.hourcalculator.domain.model.CalculationRecord
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de repositório para o histórico de cálculos.
 */
interface HistoryRepository {
    fun getHistory(): Flow<List<CalculationRecord>>
    suspend fun addRecord(record: CalculationRecord)
    suspend fun clearHistory()
}
