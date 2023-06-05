package com.jetwidgets

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt


val lightBlue = Color(173, 216, 230)

@Composable
fun RecordingButton(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    frequency: Int = 4,
    onAction: () -> Unit,
) {
    var speed by remember { mutableStateOf(1f) }
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f * speed
            }
        }
    }
    var focused by remember { mutableStateOf(false) }
    Row(
        modifier
            .padding(bottom = 24.dp)
            .size(size)
            .border(width = 1.dp, brush = SolidColor(lightBlue), shape = RoundedCornerShape(50))
            .background(
                Brush.radialGradient(
                    listOf(
                        lightBlue,
                        Color.Transparent,
                    )
                ),
                RoundedCornerShape(50)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        focused = !focused
                        speed = focused.toAnimationSpeed()
                        onAction()
                    }
                )
            }
            .clip(RoundedCornerShape(50))
            .drawWaves(time, frequency)
    ) {}
}

private fun Boolean.toAnimationSpeed() = when (this) {
    true -> 1.5f
    false -> 0.5f
}

/**
 * A modifier that draws a set of waves.
 *
 * @param time The time value to be used to calculate the position of the wave.
 * @param frequency The number of times each wave wriggles.
 */
private fun Modifier.drawWaves(time: Float, frequency: Int) = drawBehind {
    // Calculate the mean of bell curve and the distance between each wriggle on x-axis
    val mean = size.width / 2
    val pointsDistance = size.width / frequency
    // Calculate the initial offset between the three waves on x-axis
    val initialOffset = pointsDistance / 3
    // Draw the three waves with different initial offsets.
    drawWave(frequency, pointsDistance, time, mean, -initialOffset)
    drawWave(frequency, pointsDistance, time, mean, 0f)
    drawWave(frequency, pointsDistance, time, mean, initialOffset)
}

/**
 * Draws a wave using the given parameters.
 *
 * @param frequency The frequency of the sine wave to be drawn.
 * @param pointsDistance The distance between the wriggles in the wave.
 * @param time The time value to be used to calculate the position of the wave.
 * @param mean The mean value of the bel-shaped distribution curve.
 * @param initialOffset The initial offset of the wave.
 * @param heightRatio The ratio of the height of the wave to the mean value.
 */
private fun DrawScope.drawWave(
    frequency: Int,
    pointsDistance: Float,
    time: Float,
    mean: Float,
    initialOffset: Float,
    heightRatio: Float = 1f,
) {
    // The step between wriggles
    val subStep = pointsDistance / 4
    // Construct the X points of the wave using the given parameters.
    val xPoints = constructXPoints(
        frequency = frequency,
        pointsDistance = pointsDistance,
        time = time,
        initialOffset = initialOffset
    )
    // Create a path object and populate it with the cubic Bézier curves that make up the wave.
    val strokePath = Path().apply {
        for (index in xPoints.indices) {
            val offsetX = xPoints[index]
            when (index) {
                0 -> {
                    // Move to the first point in the wave.
                    val offsetY = calculateY(offsetX, mean, heightRatio)
                    moveTo(offsetX - subStep, offsetY)
                }

                xPoints.indices.last -> {
                    // Draw the last cubic Bézier curve in the wave.
                    val sourceXNeg = xPoints[index - 1] + subStep
                    val sourceYNeg = mean * 2 - calculateY(sourceXNeg, mean, heightRatio)
                    val xMiddle = (sourceXNeg + offsetX) / 2f
                    val targetOffsetX = offsetX + subStep
                    val targetOffsetY = calculateY(targetOffsetX, mean, heightRatio)
                    cubicTo(xMiddle, sourceYNeg, xMiddle, targetOffsetY, targetOffsetX, targetOffsetY)
                }

                else -> {
                    // Draw the cubic Bézier curves between the first and last points in the wave.
                    val sourceXNeg = xPoints[index - 1] + subStep
                    val sourceYNeg = mean * 2 - calculateY(sourceXNeg, mean, heightRatio)
                    val targetXPos = offsetX - subStep
                    val targetYPos = calculateY(targetXPos, mean, heightRatio)
                    val xMiddle1 = (sourceXNeg + targetXPos) / 2f
                    cubicTo(xMiddle1, sourceYNeg, xMiddle1, targetYPos, targetXPos, targetYPos)
                    val targetXNeg = offsetX + subStep
                    val targetYNeg = mean * 2 - calculateY(targetXNeg, mean, heightRatio)
                    val xMiddle2 = (targetXPos + targetXNeg) / 2f
                    cubicTo(xMiddle2, targetYPos, xMiddle2, targetYNeg, targetXNeg, targetYNeg)
                }
            }
        }
    }
    // Draw the wave path.
    drawPath(
        path = strokePath,
        color = Color.White,
        style = Stroke(
            width = 2f,
            cap = StrokeCap.Round
        )
    )
}

/**
 * Constructs an array of X points for the given wave parameters.
 *
 * @param frequency The frequency of the sine wave to be drawn.
 * @param pointsDistance The distance between the wriggles in the wave.
 * @param time The time value to be used to calculate the position of the wave.
 * @param initialOffset The initial offset of the wave.
 *
 * @return An array of X points for the given wave parameters.
 */
private fun constructXPoints(
    frequency: Int,
    pointsDistance: Float,
    time: Float,
    initialOffset: Float,
): MutableList<Float> {
    val points = mutableListOf<Float>()
    for (i in -2..frequency + 1) {
        val xMin = initialOffset + pointsDistance * i
        val addUp = time % 1 * pointsDistance
        val offsetX = xMin + addUp
        points.add(offsetX)
    }
    return points
}

/**
 * Calculates the Y-axis coordinate for the given X-coordinate and wave parameters following the shape of a normal distribution.
 *
 * @param x The X value to be used to calculate the Y value.
 * @param mean The mean value of the wave.
 * @param heightRatio The ratio of the height of the wave to the mean value.
 *
 * @return The Y value for the given X value and wave parameters.
 */
private fun calculateY(x: Float, mean: Float, heightRatio: Float): Float {
    val stdDev = mean / 3
    val exponent = -0.5 * ((x - mean) / stdDev).pow(2)
    val denominator = sqrt(2 * PI)
    return mean + (heightRatio * mean * exp(exponent) / denominator).toFloat()
}