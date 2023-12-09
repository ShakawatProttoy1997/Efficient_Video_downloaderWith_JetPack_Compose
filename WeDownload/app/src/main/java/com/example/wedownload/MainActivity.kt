package com.example.wedownload

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wedownload.ui.theme.WeDownloadTheme
import kotlinx.coroutines.launch
//import android.content.MimeType
import java.net.URLConnection
//import kotlin.text.MimeType

const val url = "https://streamable.com/5gx3rt"
class MainActivity : ComponentActivity() {
    val video_url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    var scan_uri = "content://downloads/my_downloads"
    var seekBar:MutableState<Float> = mutableStateOf(0F)


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // val downloader = DownloaderClass(this)

        //downloader.downloadFile()


        setContent {

            var scope  = rememberCoroutineScope()
            var textFieldState by remember {
                mutableStateOf("")
            }
            val snackbarHostState = remember { SnackbarHostState() }

            seekBar = remember {
                mutableStateOf(0F)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)

            ){

                Slider(value = (seekBar.value)/100, onValueChange = {
                    seekBar.value = it
                })
                TextField(
                    value =textFieldState,
                    label = {
                        Text  ("Enter Your URL..")
                    },
                    onValueChange = {
                        textFieldState = it
                    },
                    singleLine = true, modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(15.dp))
                Button(onClick = {
                    scope.launch {
                        downloadVideo(textFieldState)

                    }

                }) {
                    Text(text = "download")
                }
                Button(onClick = {
                    scope.launch {
                       // convertVideotoAnyFormat()
                        snackbarHostState.showSnackbar("convert it in your chosen format") }

                }) {
                    Text(text = "convert")
                }
                Button(onClick = {


                    // convertVideotoAnyFormat()

                }
                ) {

                    Text(text = "Pick Video")
                }



            }
            WeDownloadTheme {

            }
        }
    }

    private fun convertVideotoAnyFormat() {

    }

    private fun downloadVideo(textField:String) {
       val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE)as DownloadManager

        val request = DownloadManager.Request(Uri.parse(textField)).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                setTitle("Downloading...").setDescription("wait")
                 setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"filename.mp4")
        }
        val id  = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(id)
        val observer = object :ContentObserver(Handler(Looper.getMainLooper())){
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                 val cursor = downloadManager.query(query)
                if(cursor != null && cursor.moveToFirst()){
                    val downloaded = cursor.getLong(cursor.getColumnIndexOrThrow(
                        DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR
                    ))
                    val total = cursor.getLong(cursor.getColumnIndexOrThrow(
                        DownloadManager.COLUMN_TOTAL_SIZE_BYTES
                    ))
                    val percentage  = (downloaded*100)/total
                    seekBar.value = percentage.toFloat()
                }
            }
        }
        contentResolver.registerContentObserver(
            Uri.parse(scan_uri),true,observer
        )

    }


}

