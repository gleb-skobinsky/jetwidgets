package com.jetwidgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SiriButton(
    modifier: Modifier,
) {

}

@Composable
fun Siri(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .size(size = 250.dp)
            .background(Color.White.copy(alpha = 0.1f))
    ) {
        /*
        Box(
            modifier = Modifier
                .size(size = 451.dp)
                .background(color = Color(0xff081939).copy(alpha = 0.24f)))
        Box(
            modifier = Modifier
                .size(size = 394.dp)
                .background(color = Color(0xff081939)))
         */
        Image(
            painter = painterResource(id = R.drawable.red_blob_big),
            contentDescription = "Red Blob - Big",
            modifier = Modifier
                .fillMaxSize()
                .height(height = 289.dp)
                .align(Alignment.Center)
        )
        Image(
            painter = painterResource(id = R.drawable.green_blob_big),
            contentDescription = "Green Blob - Big",
            modifier = Modifier
                .fillMaxSize()
                .height(height = 252.dp)
                .align(Alignment.Center)
        )
        Image(
            painter = painterResource(id = R.drawable.red_vertical_intersect),
            contentDescription = "Red Vertical Intersect",
            modifier = Modifier
                .fillMaxSize()
                .height(height = 280.dp)
                .align(Alignment.Center)
        )
        /*
        Box(
            modifier = Modifier
                .size(size = 137.dp)
                .background(color = Color.White))
         */
        Image(
            painter = painterResource(id = R.drawable.green_blob_small),
            contentDescription = "Green Blob - Small",
            modifier = Modifier
                .fillMaxSize()
                .height(height = 184.dp)
                .align(Alignment.Center)
        )
        /*
        Box(
            modifier = Modifier
                .size(size = 69.dp)
                .background(color = Color.White))
         */
        Image(
            painter = painterResource(id = R.drawable.green_horizontal_intersect),
            contentDescription = "Green Horizontal Intersect",
            modifier = Modifier
                .fillMaxSize()
                .height(height = 315.dp)
                .align(Alignment.Center)
        )
    }
}