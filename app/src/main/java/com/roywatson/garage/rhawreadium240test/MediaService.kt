/*
 * Copyright 2022 Readium Foundation. All rights reserved.
 * Use of this source code is governed by the BSD-style license
 * available in the top-level LICENSE file of the project.
 */

package com.roywatson.garage.rhawreadium240test

import android.app.Application
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.media2.session.MediaSession
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import org.readium.navigator.media2.ExperimentalMedia2
import org.readium.navigator.media2.MediaNavigator


@OptIn(ExperimentalMedia2::class)
class MediaService : LifecycleMedia2SessionService() {

    /**
     * The service interface to be used by the app.
     */
    inner class Binder : android.os.Binder() {

        private val app: Application
            get() = application as Application

        private var saveLocationJob: Job? = null

        private var mediaNavigator: MediaNavigator? = null

        var mediaSession: MediaSession? = null

        fun closeNavigator() {
            stopForeground(true)
            mediaSession?.close()
            mediaSession = null
            saveLocationJob?.cancel()
            saveLocationJob = null
            mediaNavigator?.close()
            mediaNavigator?.publication?.close()
            mediaNavigator = null
        }

        @OptIn(FlowPreview::class)
        fun bindNavigator(navigator: MediaNavigator, bookId: Long) {
            val activityIntent = createSessionActivityIntent()
            mediaNavigator = navigator
            mediaSession = navigator.session(applicationContext, activityIntent)
                .also { addSession(it) }

            /*
             * Launch a job for saving progression even when playback is going on in the background
             * with no ReaderActivity opened.
             */
            // TODO:
//            saveLocationJob = navigator.currentLocator
//                .sample(3000)
//                .onEach { locator -> /*app.bookRepository.saveProgression(locator, bookId)*/ } // TODO
//                .launchIn(lifecycleScope)
        }

        private fun createSessionActivityIntent(): PendingIntent {
            // This intent will be triggered when the notification is clicked.
            var flags = PendingIntent.FLAG_UPDATE_CURRENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flags = flags or PendingIntent.FLAG_IMMUTABLE
            }

            val intent = application.packageManager.getLaunchIntentForPackage(application.packageName)
            return PendingIntent.getActivity(applicationContext, 0, intent, flags)
        }
    }

    private val binder by lazy {
        Binder()
    }

    override fun onCreate() {
        super.onCreate()
        Log.v("RHAW", "MediaService created.")
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.v("RHAW", "onBind called with $intent")

        return if (intent.action == SERVICE_INTERFACE) {
            super.onBind(intent)
            // Readium-aware client.
            Log.v("RHAW", "Returning custom binder.")
            binder
        } else {
            // External controller.
            Log.v("RHAW","Returning MediaSessionService binder.")
            super.onBind(intent)
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return binder.mediaSession
    }

    override val lifecycle: Lifecycle // TODO: wtf ???
        get() = TODO("Not yet implemented")

    override fun onDestroy() {
        super.onDestroy()
        Log.v("RHAW", "MediaService destroyed.")
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        Log.v("RHAW","Task removed. Stopping session and service.")
        // Close the navigator to allow the service to be stopped.
        binder.closeNavigator()
        stopSelf()
    }

    companion object {

        const val SERVICE_INTERFACE = "org.readium.r2.testapp.reader.MediaService"

        fun start(application: Application) {
            val intent = intent(application)
            application.startService(intent)
        }

        suspend fun bind(application: Application): MediaService.Binder {
            val mediaServiceBinder: CompletableDeferred<Binder> =
                CompletableDeferred()

            val mediaServiceConnection = object : ServiceConnection {

                override fun onServiceConnected(name: ComponentName?, service: IBinder) {
                    Log.v("RHAW", "MediaService bound.")
                    mediaServiceBinder.complete(service as MediaService.Binder)
                }

                override fun onServiceDisconnected(name: ComponentName) {
                    Log.v("RHAW","MediaService disconnected.")

                    // Should not happen, do nothing.
                }

                override fun onNullBinding(name: ComponentName) {
                    val errorMessage = "Failed to bind to MediaService."
                    Log.v("RHAW", errorMessage)
                    val exception = IllegalStateException(errorMessage)
                    mediaServiceBinder.completeExceptionally(exception)
                    // Should not happen, do nothing.
                }
            }

            val intent = intent(application)
            application.bindService(intent, mediaServiceConnection, 0)

            return mediaServiceBinder.await()
        }

        fun stop(application: Application) {
            val intent = intent(application)
            application.stopService(intent)
        }

        private fun intent(application: Application) =
            Intent(SERVICE_INTERFACE)
                // MediaSessionService.onBind requires the intent to have a non-null action
                .apply { setClass(application, MediaService::class.java) }
    }
}
