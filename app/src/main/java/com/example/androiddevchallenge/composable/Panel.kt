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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.feature.MainViewModel

@Composable
fun Panel(
    hourglassViewState: MainViewModel.PanelViewState,
    onAddMinutes: () -> Unit,
    onSubtractMinutes: () -> Unit,
    onAddSeconds: () -> Unit,
    onSubtractSeconds: () -> Unit
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
        ) {
            IconButton(
                onClick = onAddMinutes,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_up_24),
                    contentDescription = "Add minutes",
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                style = MaterialTheme.typography.h2,
                text = hourglassViewState.minutes,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            IconButton(
                onClick = onSubtractMinutes,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down_24),
                    contentDescription = "Subtract minutes",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Text(
            style = MaterialTheme.typography.h2,
            text = ":",
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically)
                .padding(
                    top = 24.dp,
                    start = 4.dp,
                    end = 4.dp
                )
        )
        Column(
            modifier = Modifier
                .wrapContentSize()
        ) {
            IconButton(
                onClick = onAddSeconds,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_up_24),
                    contentDescription = "Add seconds",
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                style = MaterialTheme.typography.h2,
                text = hourglassViewState.seconds,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            IconButton(
                onClick = onSubtractSeconds,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down_24),
                    contentDescription = "Subtract seconds",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
