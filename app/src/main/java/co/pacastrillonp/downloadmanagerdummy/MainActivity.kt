package co.pacastrillonp.downloadmanagerdummy

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class MainActivity : AppCompatActivity() {

    private var enqueue: Long = 0
    private var downloadManager: DownloadManager? = null
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
//                    val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
                    val query = DownloadManager.Query()
                    query.setFilterById(enqueue)
                    val cursor = downloadManager?.query(query)
                    if (cursor!!.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {

                            val view = findViewById<ImageView>(R.id.imageView)
                            val uriString = cursor
                                .getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                            view.setImageURI(Uri.parse(uriString))
                        }
                    }
                }
            }
        }

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }


    /** Called when the user taps the Send button  */
    @RequiresApi(Build.VERSION_CODES.N)
    fun getMedia(view: View) {
        id++
        startDownloadImage("https://eteknix-eteknixltd.netdna-ssl.com/wp-content/uploads/2015/05/space-rocket-start-wallpaper-1440x900-011.jpg", "img$id")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun startDownloadImage(url: String, mediaName: String) {
        val file = File(Environment.getExternalStorageDirectory().path + "/DownloadManagerDummy/", mediaName)
        val request = DownloadManager.Request(Uri.parse(url))
            .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
            .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
            .setAllowedOverRoaming(false)// Set if download is allowed on roaming network
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        enqueue = downloadManager!!.enqueue(request)
    }

}
