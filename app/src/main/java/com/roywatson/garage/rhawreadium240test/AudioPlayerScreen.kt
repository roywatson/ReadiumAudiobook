package com.roywatson.garage.rhawreadium240test

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.readium.navigator.media2.MediaNavigator
import org.readium.navigator.media2.ExperimentalMedia2
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalMedia2::class)
@Composable
fun AudioPlayerScreen(
    pubTitle: String,
    onPlayPause: () -> Unit,
    onJumpBack: () -> Unit,
    onJumpForward: () -> Unit,
    onClose: () -> Unit,
    playback: MediaNavigator.Playback?,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier
            .height(40.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = pubTitle,
            )
        }
        Spacer(modifier = Modifier
            .height(30.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "${playback?.resource?.position?.formatElapsedTime()} / ${playback?.resource?.duration?.formatElapsedTime()}",
            )
        }
        Spacer(modifier = Modifier
            .height(30.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                onClick = { onJumpBack() },
            ) {
                Text(
                    text = "Jump Back",
                )
            }
            Button(
                onClick = { onPlayPause() },
            ) {
                Text(
                    text = if (playback?.state == MediaNavigator.Playback.State.Playing) {
                        "Pause"
                    } else {
                        "Play"
                    },
                )
            }
            Button(
                onClick = { onJumpForward() },
            ) {
                Text(
                    text = "Jump Frwd",
                )
            }
        }
        Spacer(modifier = Modifier
            .height(50.dp)
        )
        Button(
             onClick = { onClose() }
        ) {
            Text("Close")
        }
    }
}

private fun Duration.formatElapsedTime(): String =
    DateUtils.formatElapsedTime(toLong(DurationUnit.SECONDS))

