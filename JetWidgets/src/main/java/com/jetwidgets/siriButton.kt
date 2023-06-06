package com.jetwidgets

import android.graphics.Camera
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import java.io.Serializable
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin

const val RADIUS_DP = 256f
private const val TWO_PI = Math.PI.toFloat() * 2
private const val DEFAULT_PHI = Math.PI.toFloat() / 1.7f
private const val FIELD_OF_VIEW_FACTOR = 1.0f
private const val STROKE_TRANSFORMATION_OFFSET = 35f //considering phi value of π/1.7

@Composable
fun BoxScope.SiriButton() {
    // var normalized = 0f
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f
            }
        }
    }
    Row(
        Modifier
            .align(Alignment.Center)
            .size(RADIUS_DP.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.2f))
    ) {
        val point = RotatingPoint(phi = DEFAULT_PHI, theta = normalize(time, 0f, 60f, 0f, TWO_PI))
        Canvas(Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.Magenta,
                radius = 10.dp.toPx(),
                center = Offset(size.width / 2, size.height / 2)
            )
            val rectSize = 40.dp.toPx()
            val pointIn2D = map2Dto3D(point.phi, point.theta, time, RADIUS_DP.dp.toPx())
            scale(pointIn2D.projectedScale) {
                val matrix = android.graphics.Matrix()
                val camera = Camera()
                val x = pointIn2D.projectedX
                val y = pointIn2D.projectedY + round(size.minDimension / 100)
                drawIntoCanvas { canvas ->
                    applyStrokeTransformations(pointIn2D.projectedX, size, camera)
                    camera.getMatrix(matrix)
                    matrix.preTranslate(-x, -y)
                    matrix.postTranslate(x, y)
                    canvas.save()
                    canvas.nativeCanvas.concat(matrix)
                    drawRect(
                        color = Color.Red,
                        size = Size(rectSize, rectSize),
                        topLeft = Offset(pointIn2D.projectedX - rectSize / 2, pointIn2D.projectedY - rectSize / 2)
                    )
                    canvas.restore()
                }
            }
        }
    }
    Column(Modifier.align(Alignment.BottomCenter)) {
        Text(
            text = String.format("%.4f", time),
            color = Color.White,
        )
    }
}

private data class Info2D(val projectedX: Float, val projectedY: Float, val projectedScale: Float) : Serializable

private fun DrawScope.map2Dto3D(phi: Float, theta: Float, rotationY: Float, radius: Float): Info2D {
    // Calculate coordinates in 3D plane.
    val x = radius * sin(phi) * cos(theta)
    val y = radius * cos(phi)
    val z = radius * sin(phi) * sin(theta) - radius

    // Rotate 3D coordinates about the y-axis.
    val rotatedX = cos(rotationY) * x + sin(rotationY) * (z + radius)
    val rotatedZ = -sin(rotationY) * x + cos(rotationY) * (z + radius) - radius

    // Project the rotated 3D coordinates onto the 2D canvas.
    val fieldOfView = size.minDimension * FIELD_OF_VIEW_FACTOR
    val projectedScale = fieldOfView / (fieldOfView - rotatedZ)
    val projectedX: Float = ((rotatedX * projectedScale) + size.width / 2f)
    val projectedY: Float = ((y * projectedScale) + size.height / 2f)

    return Info2D(projectedX, projectedY, projectedScale)
}

fun normalize(value: Float, fromMin: Float, fromMax: Float, toMin: Float, toMax: Float): Float {
    //[min,max] to [a,b] >>> f(x) = (b - a) (x - min) / (max - min) + a
    val v = when {
        value > fromMax -> fromMax
        value < fromMin -> fromMin
        else -> value
    }
    return (toMax - toMin) * (v - fromMin) / (fromMax - fromMin) + toMin
}

data class RotatingPoint(
    val phi: Float,
    val theta: Float,
)

private fun applyStrokeTransformations(projectedX: Float, canvasDim: Size, camera: Camera) {
    val canvasCenterX = canvasDim.center.x
    val canvasStartX = canvasCenterX - canvasDim.minDimension / 2
    val canvasEndX = canvasCenterX + canvasDim.minDimension / 2
    val canvasStartOffsetX = canvasStartX + STROKE_TRANSFORMATION_OFFSET
    val canvasEndOffsetX = canvasEndX - STROKE_TRANSFORMATION_OFFSET
    if (projectedX < canvasCenterX) {
        camera.rotate(
            0f,
            normalize(projectedX, canvasStartX, canvasCenterX, 80f, 0f),
            normalize(projectedX, canvasStartX, canvasStartOffsetX, -35f, 0f)
        )
    } else {
        camera.rotate(
            0f,
            normalize(projectedX, canvasCenterX, canvasEndX, 0f, -80f),
            normalize(projectedX, canvasEndOffsetX, canvasEndX, 0f, 35f)
        )
    }
}