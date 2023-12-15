package com.roywatson.garage.rhawreadium240test

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.readium.navigator.media2.ExperimentalMedia2
import org.readium.navigator.media2.MediaNavigator
import org.readium.r2.navigator.ExperimentalAudiobook
import org.readium.r2.shared.publication.Publication
import org.readium.r2.shared.publication.asset.FileAsset
import org.readium.r2.shared.util.getOrElse
import org.readium.r2.streamer.Streamer
import java.io.File

class AudioRepository(
    private val application: Application,
    private val streamer: Streamer,
) {

    var publication: Publication? = null

    @OptIn(ExperimentalAudiobook::class, ExperimentalMedia2::class)
    var navigator : MediaNavigator? = null
    var mediaBinder : MediaService.Binder? = null

    var filePath: MutableState<String> = mutableStateOf("")

    private val _isAudioSetup = MutableStateFlow(false)
    val isAudioSetup: StateFlow<Boolean> = _isAudioSetup

    @OptIn(ExperimentalAudiobook::class, ExperimentalMedia2::class)
    suspend fun openBook(
        path: String,
    ): Boolean {
        return try {
            val file = File(path)
            val asset = FileAsset(file)
            publication = streamer.open(asset, allowUserInteraction = true).getOrThrow()
            true
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }

    @OptIn(ExperimentalMedia2::class)
    suspend fun setupAudio() {
        if(navigator != null) { return }

        try {
            navigator = publication?.let {
                MediaNavigator.create(application, it, null)
                    .getOrElse { throw Exception("Cannot open Audiobook") }
            }
            MediaService.start(application)
            val binder = MediaService.bind(application)
            navigator?.let {
                binder.bindNavigator(
                    it,
                    0L
                ) // TODO: bookId is only used to save progress,
                    // so forcing to 0 should not effect us here
                    // And we will be implementing our own mechanism anyway.
            }
            mediaBinder = binder
            _isAudioSetup.value = true
        } catch(ex: Exception) {
            ex.printStackTrace()
        }
    }

    @OptIn(ExperimentalMedia2::class)
    fun teardownAudio() {
        mediaBinder?.closeNavigator()
        MediaService.stop(application)
        publication?.close()
        publication = null
        mediaBinder = null
        navigator = null
        _isAudioSetup.value = false
    }
}