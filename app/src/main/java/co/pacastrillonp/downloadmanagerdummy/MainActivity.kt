package co.pacastrillonp.downloadmanagerdummy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import android.widget.Toast
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.IntentFilter


class MainActivity : AppCompatActivity() {
    private var downloadID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onDownloadComplete)
    }

    /** Called when the user taps the Send button  */
    @RequiresApi(Build.VERSION_CODES.N)
    fun getImage(view: View) {
        beginDownload()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun beginDownload(){
        val file = File(getExternalFilesDir(null), "Dummy")
        /*
       Create a DownloadManager.Request with all the information necessary to start the download
        */
        val request = DownloadManager.Request(Uri.parse("http://i.imgur.com/cEdWOV6.jpg"))
            .setTitle("Dummy File")// Title of the Download Notification
            .setDescription("Downloading")// Description of the Download Notification
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
            .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
            .setRequiresCharging(false)// Set if charging is required to begin the download
            .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
            .setAllowedOverRoaming(true)// Set if download is allowed on roaming network
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Fetching the download id received with the broadcast
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(this@MainActivity, "Download Completed", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
