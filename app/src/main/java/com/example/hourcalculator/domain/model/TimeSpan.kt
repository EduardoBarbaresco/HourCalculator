package com.example.hourcalculator.domain.model

import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToLong

/**
 * Representação imutável de uma duração de tempo.
 * Armazenada internamente em minutos com precisão Double para suportar frações e evitar perda de precisão.
 */
data class TimeSpan(val totalMinutes: Double) {

    val isNegative: Boolean get() = totalMinutes < 0.0

    val absoluteMinutes: Double get() = abs(totalMinutes)

    /**
     * Total de minutos arredondado para cálculos de relógio.
     */
    val roundedMinutes: Long get() = absoluteMinutes.roundToLong()

    /**
     * Horas inteiras da duração.
     */
    val hours: Long get() = roundedMinutes / 60

    /**
     * Minutos restantes (0 a 59).
     */
    val minutes: Long get() = roundedMinutes % 60

    /**
     * Valor em Horas Centesimais (Decimais), ex: 1h30 = 1.50.
     */
    val centesimalHours: Double get() = totalMinutes / 60.0

    /**
     * Retorna a representação formatada no padrão Sexagesimal (HH:MM).
     * Exemplo: "01:30", "00:45", "-02:15"
     */
    fun formatSexagesimal(): String {
        val sign = if (isNegative) "-" else ""
        return String.format(Locale.US, "%s%02d:%02d", sign, hours, minutes)
    }

    /**
     * Retorna a representação formatada no padrão Centesimal / Decimal com 2 casas decimais.
     * Exemplo: "1,50 h", "0,75 h", "-2,25 h"
     */
    fun formatCentesimal(): String {
        return String.format(Locale.GERMANY, "%.2f h", centesimalHours)
    }

    /**
     * Retorna apenas o número centesimal formatado (sem a unidade 'h').
     * Exemplo: "1,50"
     */
    fun formatCentesimalNumber(): String {
        return String.format(Locale.GERMANY, "%.2f", centesimalHours)
    }

    operator fun plus(other: TimeSpan): TimeSpan = TimeSpan(totalMinutes + other.totalMinutes)
    operator fun minus(other: TimeSpan): TimeSpan = TimeSpan(totalMinutes - other.totalMinutes)
    operator fun times(scalar: Double): TimeSpan = TimeSpan(totalMinutes * scalar)
    operator fun div(scalar: Double): TimeSpan = TimeSpan(totalMinutes / scalar)
    operator fun div(other: TimeSpan): Double = if (other.totalMinutes == 0.0) 0.0 else totalMinutes / other.totalMinutes

    companion object {
        val ZERO = TimeSpan(0.0)

        fun fromHoursAndMinutes(hours: Long, minutes: Long, isNegative: Boolean = false): TimeSpan {
            val total = (hours * 60.0 + minutes) * (if (isNegative) -1.0 else 1.0)
            return TimeSpan(total)
        }

        fun fromCentesimalHours(hours: Double): TimeSpan {
            return TimeSpan(hours * 60.0)
        }
    }
}
