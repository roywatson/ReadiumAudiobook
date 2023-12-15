package com.roywatson.garage.rhawreadium240test

import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.readium.r2.streamer.Streamer

val audioModule = module {

    single {
        Streamer(androidContext(), emptyList())
//        AudioRepository(androidApplication() as Application, get())
    }

    single<AudioRepository> { AudioRepository(androidApplication() as Application, get()) }

    viewModel { AudioViewModel(get()) }
}