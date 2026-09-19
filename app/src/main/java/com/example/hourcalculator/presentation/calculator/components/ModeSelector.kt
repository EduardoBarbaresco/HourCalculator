package com.example.hourcalculator.presentation.calculator.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.hourcalculator.domain.model.CalculatorMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeSelector(
    currentMode: CalculatorMode,
    onModeSelected: (CalculatorMode) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        CalculatorMode.entries.forEachIndexed { index, mode ->
            val isSelected = currentMode == mode
            SegmentedButton(
                selected = isSelected,
                onClick = { onModeSelected(mode) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = CalculatorMode.entries.size
                ),
                label = {
                    Text(
                        text = mode.shortLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                },
                icon = {
                    SegmentedButtonDefaults.Icon(active = isSelected) {
                        Icon(
                            imageVector = if (mode == CalculatorMode.SEXAGESIMAL_TO_DECIMAL)
                                Icons.Default.Schedule else Icons.Default.Calculate,
                            contentDescription = null,
                            modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                        )
                    }
                }
            )
        }
    }
}
