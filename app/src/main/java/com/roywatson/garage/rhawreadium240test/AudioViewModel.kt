package com.roywatson.garage.rhawreadium240test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioViewModel(
    val repository : AudioRepository,
) : ViewModel() {

    fun open(path: String) {
        val job = viewModelScope.launch(Dispatchers.Main) {
            try {
                repository.openBook(path)
                withContext(Dispatchers.Main) {
                    repository.setupAudio()
                }
            } catch(ex: Exception) {
                ex.printStackTrace()
            }
        }

    }
}