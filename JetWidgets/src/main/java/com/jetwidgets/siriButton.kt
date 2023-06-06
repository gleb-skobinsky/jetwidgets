package com.jetwidgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun BoxScope.SiriButton() {
    Row(
        Modifier
            .align(Alignment.Center)
            .size(256.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.2f))
    ) {

    }
}