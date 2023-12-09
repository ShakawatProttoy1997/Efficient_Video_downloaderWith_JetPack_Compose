 package com.example.wedownload

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log.d
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.transformer.Composition
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.Transformer
import java.io.File

 class DownloadCompletedReceiver:BroadcastReceiver() {
    val inputMediaItem = MediaItem.fromUri(url)


    @OptIn(UnstableApi::class) override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == "android.intent.action.DOWNLOAD_COMPLETE"){
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1L)
            if(id != -1L){

                d("Tag","download with id: $id finished...")

                val editedMediaItem =
                    EditedMediaItem.Builder(inputMediaItem).setRemoveVideo(true).build()
                if (context != null) {
                    val transformer = Transformer.Builder(context)
                        .setVideoMimeType(MimeTypes.VIDEO_MP4)
                        .setAudioMimeType(MimeTypes.AUDIO_AAC)
                        .addListener(transformerListener)
                        .build()
                    //val outputPath = File(context?.cacheDir, "output.mp3").path
                    //transformer.start(editedMediaItem, outputPath)
                    transformer.start(editedMediaItem,"/storage/emulated/0/Download")
                }

            }
            else  d("Tag","download with id: $id ")
        }

    }

    val transformerListener: Transformer.Listener =
        @UnstableApi object : Transformer.Listener {
            override fun onCompleted(composition: Composition, result: ExportResult) {

                playOutput()
            }

            override fun onError(composition: Composition, result: ExportResult,
                                 exception: ExportException
            ) {
                displayError(exception)
            }
        }


    private fun displayError(exception: ExportException) {

    }

    private fun playOutput() {
        TODO("Not yet implemented")
    }

}