package com.roywatson.garage.rhawreadium240test

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.readium.r2.shared.extensions.tryOrNull
import org.readium.r2.shared.publication.asset.FileAsset
import org.readium.r2.shared.util.mediatype.MediaType
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.UUID

class Application : android.app.Application() {

    private val nyplDirectory: String
        get() = "${filesDir?.path}/"

    var sampleFilePath: String? = null
        private set

    val model: AudioViewModel by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(audioModule)
        }

        MainScope().async {
             val sampleFile =
                assets.open("blackcat0312sep1898_1_various_64kb.mp3").copyToTempFile(nyplDirectory)
            sampleFile?.let { file ->
                val pubAsset = FileAsset(file, MediaType.MP3)
                val mediaType = pubAsset.mediaType()
                val filename = "${UUID.randomUUID()}.${mediaType.fileExtension}"
                val libAsset = FileAsset(File("$nyplDirectory$filename"), mediaType)
                pubAsset.file.moveTo(libAsset.file)
                sampleFilePath = libAsset.file.absolutePath
                model.repository.filePath.value = sampleFilePath ?: "error"
                Log.v("RHAW", "sample path=${sampleFilePath ?: "null"}")
            }
        }
    }
}

suspend fun InputStream.toFile(path: String) {
    withContext(Dispatchers.IO) {
        use { input ->
            File(path).outputStream().use { input.copyTo(it) }
        }
    }
}

suspend fun InputStream.copyToTempFile(dir: String): File? = tryOrNull {
    val filename = UUID.randomUUID().toString()
    File(dir + filename)
        .also { toFile(it.path) }
}

suspend fun File.moveTo(target: File) = withContext(Dispatchers.IO) {
    if (!this@moveTo.renameTo(target)) {
        throw IOException()
    }
}

