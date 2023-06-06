package com.jetwidgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    val screenMode = remember { mutableStateOf(ScreenMode.SINE) }
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button({ screenMode.value = ScreenMode.SINE }) { Text("Sine wave") }
            Button({ screenMode.value = ScreenMode.SIRI }) { Text("Siri") }
        }
        when (screenMode.value) {
            ScreenMode.SINE -> SineScreen()
            ScreenMode.SIRI -> {
                SiriButton()
            }
        }

    }
}

@Composable
private fun BoxScope.SineScreen() {
    Column(
        modifier = Modifier.Companion.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var size by remember { mutableStateOf(24f) }
        var frequency by remember { mutableStateOf(4f) }
        Text("Set size:")
        Slider(
            value = size,
            onValueChange = { size = it },
            valueRange = 0f..400f
        )
        Text("Set wave frequency:")
        Slider(
            value = frequency,
            onValueChange = { frequency = it },
            valueRange = 0f..50f
        )
        RecordingButton(Modifier, size.dp, frequency.toInt()) {}
    }
}

private enum class ScreenMode {
    SINE,
    SIRI
}
