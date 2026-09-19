package com.example.hourcalculator.domain.model

/**
 * Modo de operação da calculadora.
 * - [SEXAGESIMAL_TO_DECIMAL]: Entrada em formato HH:MM, visor principal em HH:MM e conversão secundária em Centesimal.
 * - [DECIMAL_TO_SEXAGESIMAL]: Entrada em números decimais, visor principal em Decimal e conversão secundária em HH:MM.
 */
enum class CalculatorMode(val title: String, val shortLabel: String) {
    SEXAGESIMAL_TO_DECIMAL("Horas (HH:MM)", "HH:MM → Centesimal"),
    DECIMAL_TO_SEXAGESIMAL("Decimal (0,00h)", "Centesimal → HH:MM")
}
