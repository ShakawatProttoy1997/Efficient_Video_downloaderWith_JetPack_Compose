package com.example.wedownload

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import javax.xml.transform.Transformer

class DownloaderClass(private val context: Context):Downloader{
    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    override fun downloadFile(): Long {


        val request = DownloadManager.Request(url.toUri()).setMimeType("video/mp4")
           .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
           .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
           .setTitle("video.mp4").addRequestHeader("Authorizaton","Bearer<Token>")
           .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"video.mp4")
           return downloadManager.enqueue(request)
    }


}