package com.example.hourcalculator.domain.model

/**
 * Operações matemáticas suportadas na calculadora de horas.
 */
enum class Operator(val symbol: String, val displaySymbol: String) {
    ADD("+", "+"),
    SUBTRACT("-", "-"),
    MULTIPLY("*", "×"),
    DIVIDE("/", "÷");

    companion object {
        fun fromSymbol(char: Char): Operator? = when (char) {
            '+' -> ADD
            '-' -> SUBTRACT
            '*', '×', 'x', 'X' -> MULTIPLY
            '/', '÷', ':' -> DIVIDE
            else -> null
        }

        fun fromString(str: String): Operator? {
            val trimmed = str.trim()
            return entries.firstOrNull { it.symbol == trimmed || it.displaySymbol == trimmed }
        }
    }
}
