package com.roywatson.garage.rhawreadium240test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun AudioOpenScreen(
    path: String,
    onOpen: (String) -> Unit,
    isAudioSetup: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.app_name))
        Spacer(modifier = Modifier
            .height(30.dp)
        )
        Button(
            enabled = path.isNotEmpty() and !isAudioSetup,
            onClick = { onOpen(path) }
        ) {
            Text("Open part 1")
        }
    }
}