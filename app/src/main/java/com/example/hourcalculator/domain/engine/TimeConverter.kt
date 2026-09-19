package com.example.hourcalculator.domain.engine

import com.example.hourcalculator.domain.model.TimeSpan
import java.util.Locale
import kotlin.math.roundToLong

/**
 * Utilitário de conversão pura entre representações de tempo sexagesimal (HH:MM) e centesimal/decimal.
 */
object TimeConverter {

    /**
     * Converte uma string no formato "HH:MM" ou "-HH:MM" para [TimeSpan].
     */
    fun parseSexagesimal(input: String): TimeSpan? {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return null

        val isNegative = trimmed.startsWith("-")
        val clean = if (isNegative) trimmed.substring(1) else trimmed

        val parts = clean.split(":")
        if (parts.size != 2) return null

        val hours = parts[0].toLongOrNull() ?: return null
        val minutes = parts[1].toLongOrNull() ?: return null

        return TimeSpan.fromHoursAndMinutes(hours, minutes, isNegative)
    }

    /**
     * Converte uma string decimal (ex: "1.5", "1,50", "-0,75") para [TimeSpan].
     */
    fun parseDecimal(input: String): TimeSpan? {
        val trimmed = input.trim().replace(',', '.')
        if (trimmed.isEmpty()) return null

        val decimalHours = trimmed.toDoubleOrNull() ?: return null
        return TimeSpan.fromCentesimalHours(decimalHours)
    }

    /**
     * Converte minutos totais para horas decimais com 2 casas decimais.
     */
    fun minutesToCentesimal(totalMinutes: Double): Double {
        return (totalMinutes / 60.0 * 100.0).roundToLong() / 100.0
    }

    /**
     * Formata um valor double em padrão centesimal com 2 casas (ex: "1,50 h").
     */
    fun formatCentesimal(value: Double): String {
        return String.format(Locale.GERMANY, "%.2f h", value)
    }

    /**
     * Formata minutos totais no formato "HH:MM".
     */
    fun formatSexagesimal(totalMinutes: Double): String {
        return TimeSpan(totalMinutes).formatSexagesimal()
    }
}
