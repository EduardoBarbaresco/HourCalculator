package com.example.hourcalculator.presentation.calculator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hourcalculator.domain.model.CalculatorMode
import com.example.hourcalculator.ui.theme.CentesimalBadgeBg

@Composable
fun DisplaySection(
    expressionTape: String,
    primaryDisplay: String,
    secondaryDisplay: String,
    operandContext: String,
    mode: CalculatorMode,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Linha da Expressão Anterior / Fita
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = expressionTape.ifEmpty { " " },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            // Contexto do Operando Digitado (Equivalência inline discreta)
            if (operandContext.isNotEmpty()) {
                Text(
                    text = operandContext,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            // Display Principal (Sexagesimal ou Centesimal conforme o modo)
            Text(
                text = errorMessage ?: primaryDisplay,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 44.sp),
                color = if (errorMessage != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                maxLines = 1
            )

            // Badge Secundário de Conversão em Tempo Real
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(CentesimalBadgeBg.copy(alpha = 0.15f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Conversão",
                        tint = CentesimalBadgeBg,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                    val label = if (mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL) "Centesimal:" else "Horas/Min:"
                    Text(
                        text = "$label $secondaryDisplay",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                        color = CentesimalBadgeBg,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
