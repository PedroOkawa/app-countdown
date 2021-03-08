/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.composable

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.feature.MainViewModel

@Composable
fun Hourglass(
    hourglassViewState: MainViewModel.HourglassViewState
) {
    val contentColor = MaterialTheme.colors.onPrimary
    val bodyColor = MaterialTheme.colors.primaryVariant

    val density = LocalDensity.current.density

    val hourglassBodyHeight = 320f
    val hourglassBodyWidth = 240f
    val hourglassContentHeight = 300f
    val hourglassContentWidth = 200f
    val timeInMillis: Long
    val baseX: Float
    val totalIterations: Long
    var rotate = false
    var rotationDegrees = 0f
    val partHeight = hourglassContentHeight * 0.5f

    when (hourglassViewState) {
        is MainViewModel.HourglassViewState.Countdown -> {
            timeInMillis = hourglassViewState.timeInMillis
            totalIterations = hourglassViewState.totalIterations
            baseX = ((hourglassContentWidth * 0.5f) / totalIterations)
        }
        is MainViewModel.HourglassViewState.Idle -> {
            timeInMillis = hourglassViewState.timeInMillis
            totalIterations = hourglassViewState.totalIterations
            baseX = ((hourglassContentWidth * 0.5f) / totalIterations)
            rotate = hourglassViewState.rotate
            rotationDegrees = hourglassViewState.rotationDegrees
        }
    }

    val rotateState by animateFloatAsState(
        targetValue = rotationDegrees,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )

    val modifier = if (rotate) {
        Modifier.rotate(rotateState)
    } else Modifier

    val hourglassBodyPath = Path().apply {
        moveTo(
            x = 0f,
            y = 0f
        )
        lineTo(
            x = hourglassBodyWidth * density,
            y = 0f
        )
        lineTo(
            x = hourglassBodyWidth * density * 0.55f,
            y = hourglassBodyHeight * density * 0.5f
        )
        lineTo(
            x = hourglassBodyWidth * density,
            y = hourglassBodyHeight * density
        )
        lineTo(
            x = 0f,
            y = hourglassBodyHeight * density
        )
        lineTo(
            x = hourglassBodyWidth * density * 0.45f,
            y = hourglassBodyHeight * density * 0.5f
        )
        close()
    }

    val hourglassTopPath = Path().apply {
        moveTo(
            x = baseX * (totalIterations - timeInMillis) * density,
            y = ((partHeight) / totalIterations) * (totalIterations - timeInMillis) * density
        )
        lineTo(
            x = hourglassContentWidth * density * 0.5f,
            y = partHeight * density
        )
        lineTo(
            x = (hourglassContentWidth - baseX * (totalIterations - timeInMillis)) * density,
            y = ((partHeight) / totalIterations) * (totalIterations - timeInMillis) * density
        )
        close()
    }

    val hourglassBottomPath = Path().apply {
        moveTo(
            x = 0f,
            y = hourglassContentHeight * density
        )
        lineTo(
            x = baseX * (totalIterations - timeInMillis) * density,
            y = (partHeight + ((partHeight) / totalIterations) * (timeInMillis)) * density
        )
        lineTo(
            x = (hourglassContentWidth - (baseX * (totalIterations - timeInMillis))) * density,
            y = (partHeight + (((partHeight) / totalIterations) * (timeInMillis))) * density
        )
        lineTo(
            x = hourglassContentWidth * density,
            y = hourglassContentHeight * density
        )
        close()
    }

    Column(
        modifier = Modifier.wrapContentSize()
    ) {
        Box(
            modifier = modifier.align(alignment = Alignment.CenterHorizontally)
        ) {
            Canvas(
                modifier = Modifier
                    .width(hourglassBodyWidth.dp)
                    .height(hourglassBodyHeight.dp)
                    .align(Alignment.Center)
            ) {
                drawPath(hourglassBodyPath, bodyColor)
            }
            Canvas(
                modifier = Modifier
                    .width(hourglassContentWidth.dp)
                    .height(hourglassContentHeight.dp)
                    .align(Alignment.Center)
            ) {
                drawPath(hourglassTopPath, contentColor)
                drawPath(hourglassBottomPath, contentColor)
            }
        }
    }
}
