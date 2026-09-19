package com.example.hourcalculator.domain.engine

import java.util.Locale

/**
 * Gerencia a formatação dinâmica e inteligente de entrada de dígitos.
 */
object TimeInputMask {

    /**
     * Aplica máscara de tempo sequencial a uma sequência de dígitos.
     * Exemplo:
     * - "" -> "00:00"
     * - "3" -> "00:03"
     * - "30" -> "00:30"
     * - "130" -> "01:30"
     * - "1230" -> "12:30"
     * - "12030" -> "120:30"
     */
    fun formatSexagesimalDigits(rawDigits: String): String {
        val cleanDigits = rawDigits.filter { it.isDigit() }.trimStart('0')
        if (cleanDigits.isEmpty()) return "00:00"

        return when (cleanDigits.length) {
            1 -> "00:0$cleanDigits"
            2 -> "00:$cleanDigits"
            3 -> "0${cleanDigits[0]}:${cleanDigits.substring(1)}"
            4 -> "${cleanDigits.substring(0, 2)}:${cleanDigits.substring(2)}"
            else -> {
                val hours = cleanDigits.substring(0, cleanDigits.length - 2)
                val minutes = cleanDigits.substring(cleanDigits.length - 2)
                "$hours:$minutes"
            }
        }
    }

    /**
     * Extrai os dígitos puros de uma string formatada em HH:MM.
     */
    fun extractRawDigits(formatted: String): String {
        return formatted.filter { it.isDigit() }
    }

    /**
     * Converte os dígitos brutos diretamente para o total de minutos.
     */
    fun rawDigitsToMinutes(rawDigits: String): Double {
        val formatted = formatSexagesimalDigits(rawDigits)
        val parts = formatted.split(":")
        val hours = parts.getOrNull(0)?.toLongOrNull() ?: 0L
        val minutes = parts.getOrNull(1)?.toLongOrNull() ?: 0L
        return hours * 60.0 + minutes
    }

    /**
     * Formata uma entrada decimal com vírgula ou ponto.
     */
    fun formatDecimalInput(input: String): String {
        if (input.isEmpty()) return "0,00"
        return input.replace('.', ',')
    }
}
