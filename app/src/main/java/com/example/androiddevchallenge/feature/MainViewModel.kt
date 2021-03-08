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
package com.example.androiddevchallenge.feature

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs

private const val ROTATION_DEGREES = 180f
private const val ONE_HOUR_IN_MILLIS = 1000L * 60L * 60L
private const val animationPace = 100L
private const val PATTERN_MINUTES = "mm"
private const val PATTERN_SECONDS = "ss"
private const val DEFAULT_TIME_VALUE = "00"

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    data class PanelViewState(
        val minutes: String = DEFAULT_TIME_VALUE,
        val seconds: String = DEFAULT_TIME_VALUE
    )

    sealed class HourglassViewState {
        data class Countdown(
            val timeInMillis: Long,
            val totalIterations: Long
        ) : HourglassViewState()
        data class Idle(
            val timeInMillis: Long = 1L,
            val totalIterations: Long = 1L,
            val rotate: Boolean = false,
            val rotationDegrees: Float = ROTATION_DEGREES
        ) : HourglassViewState()
    }

    private val minutesDateFormat = SimpleDateFormat(PATTERN_MINUTES, Locale.getDefault())
    private val secondsDateFormat = SimpleDateFormat(PATTERN_SECONDS, Locale.getDefault())

    private var timeInMillis: Long = 0L

    private val _hourglassViewState = MutableStateFlow<HourglassViewState>(HourglassViewState.Idle())
    val hourglassViewState: StateFlow<HourglassViewState> = _hourglassViewState

    private val _panelState = MutableStateFlow(PanelViewState())
    val panelState: StateFlow<PanelViewState> = _panelState

    fun addMinute() {
        if (_hourglassViewState.value is HourglassViewState.Idle) {
            timeInMillis += 1000 * 60
            postPanelState(timeInMillis)
        }
    }

    fun addSeconds() {
        if (_hourglassViewState.value is HourglassViewState.Idle) {
            timeInMillis += 1000
            postPanelState(timeInMillis)
        }
    }

    fun subtractMinute() {
        if (_hourglassViewState.value is HourglassViewState.Idle) {
            timeInMillis -= 1000 * 60
            postPanelState(timeInMillis)
        }
    }

    fun subtractSeconds() {
        if (_hourglassViewState.value is HourglassViewState.Idle) {
            timeInMillis -= 1000
            postPanelState(timeInMillis)
        }
    }

    fun executeTimer() {
        convertToPositiveTime()
        if (timeInMillis > 0L && _hourglassViewState.value is HourglassViewState.Idle) {
            val totalTime = timeInMillis / animationPace
            var time = totalTime

            object : CountDownTimer(timeInMillis, animationPace) {
                override fun onTick(millisUntilFinished: Long) {
                    val currentTimeInMillis = time-- * animationPace
                    _hourglassViewState.value = HourglassViewState.Countdown(
                        currentTimeInMillis,
                        totalTime * animationPace
                    )
                    postPanelState(currentTimeInMillis)
                }

                override fun onFinish() {
                    timeInMillis = 0
                    _hourglassViewState.value = HourglassViewState.Countdown(timeInMillis, totalTime * animationPace)
                    _hourglassViewState.value = HourglassViewState.Idle(totalIterations = totalTime * animationPace, rotate = true)
                    postPanelState(timeInMillis)
                }
            }.start()
        }
    }

    private fun postPanelState(time: Long) {
        _panelState.value = PanelViewState(
            minutesDateFormat.format(time),
            secondsDateFormat.format(time)
        )
    }

    private fun convertToPositiveTime() {
        if (timeInMillis < 0L) {
            timeInMillis = ONE_HOUR_IN_MILLIS - abs(timeInMillis % ONE_HOUR_IN_MILLIS)
        } else if (timeInMillis > 0L) {
            timeInMillis %= ONE_HOUR_IN_MILLIS
        }
    }
}
