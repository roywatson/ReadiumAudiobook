package com.roywatson.garage.rhawreadium240test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.roywatson.garage.rhawreadium240test.ui.theme.RHAWReadium240TestTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.readium.navigator.media2.ExperimentalMedia2
import org.readium.navigator.media2.MediaNavigator
import kotlin.time.ExperimentalTime

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMedia2::class, ExperimentalTime::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: AudioViewModel by inject()

        setContent {
            RHAWReadium240TestTheme {
                val navController = rememberNavController()
                val isAudioSetup = model.repository.isAudioSetup.collectAsState()

                NavHost(navController = navController, startDestination = "openScreen") {
                    composable(route = "openScreen") {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            val path = model.repository.filePath

                            if(isAudioSetup.value) {
                                navController.navigate("playerScreen")
                            } else {
                                AudioOpenScreen(
                                    path = path.value,
                                    onOpen = { pathToOpen -> model.open(pathToOpen) },
                                    isAudioSetup = isAudioSetup.value,
                                )
                            }
                        }
                    }
                    composable(route = "playerScreen") {
                        val playback = model.repository.navigator?.playback?.collectAsState()
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            AudioPlayerScreen(
                                pubTitle = "",
                                onPlayPause = {
                                    when (playback?.value?.state) {
                                        MediaNavigator.Playback.State.Playing -> {
                                            model.viewModelScope.launch {
                                                model.repository.navigator?.pause()
                                            }
                                        }
                                        MediaNavigator.Playback.State.Paused -> {
                                            model.viewModelScope.launch {
                                                model.repository.navigator?.play()
                                            }
                                        }
                                        MediaNavigator.Playback.State.Finished -> {}
                                        MediaNavigator.Playback.State.Error -> {}
                                        else -> {}
                                    }
                                },
                                onJumpBack = {
                                    model.viewModelScope.launch {
                                        model.repository.navigator?.goBackward()
                                    }
                                },
                                onJumpForward = {
                                    model.viewModelScope.launch {
                                        model.repository.navigator?.goForward()
                                    }
                                },
                                onClose = {
                                    model.repository.teardownAudio()
                                    navController.navigate("openScreen")
                                },
                                playback = playback?.value,
                            )
                        }
                    }
                }
            }
        }
    }
}
