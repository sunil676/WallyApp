package com.sunil.wallyapp.service

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sunil.wallyapp.R
import com.sunil.wallyapp.data.RetrofitBuilder
import com.sunil.wallyapp.utils.AppConstant.PROGRESS_UPDATE
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.*
import java.util.*


class DownloadNotificationService : IntentService("Service") {

    private var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null
    private var url: String? = null

    override fun onHandleIntent(intent: Intent?) {
        url = intent?.getStringExtra("URL");
        notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("id", "an", NotificationManager.IMPORTANCE_LOW)
            notificationChannel.description = "no sound"
            notificationChannel.setSound(null, null)
            notificationChannel.enableLights(false)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(false)
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
        notificationBuilder = NotificationCompat.Builder(this, "id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Download")
            .setContentText("Downloading Image..")
            .setDefaults(0)
            .setAutoCancel(true)
        notificationManager!!.notify(0, notificationBuilder!!.build())
        initRetrofit(url!!)
    }

    private fun initRetrofit(url: String) {
        val request: Call<ResponseBody> = RetrofitBuilder.apiService.downloadImage(url)
        try {
            downloadImage(request.execute().body()!!)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(IOException::class)
    private fun downloadImage(body: ResponseBody) {
        var count: Int = 0
        val data = ByteArray(1024 * 4)
        val fileSize = body.contentLength()
        val inputStream: InputStream = BufferedInputStream(body.byteStream(), 1024 * 8)
        val outputFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            UUID.randomUUID().toString() + ".jpg"
        )

        val outputStream: OutputStream = FileOutputStream(outputFile)
        var total: Long = 0
        var downloadComplete = false
        //int totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
        while (inputStream.read(data).also({ count = it }) != -1) {
            total += count.toLong()
            val progress = ((total * 100).toDouble() / fileSize.toDouble()).toInt()
            updateNotification(progress)
            outputStream.write(data, 0, count)
            downloadComplete = true
        }
        onDownloadComplete(downloadComplete)
        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }

    private fun updateNotification(currentProgress: Int) {
        notificationBuilder!!.setProgress(100, currentProgress, false)
        notificationBuilder!!.setContentText("Downloaded: $currentProgress%")
        notificationManager!!.notify(0, notificationBuilder!!.build())
    }


    private fun sendProgressUpdate(downloadComplete: Boolean) {
        val intent = Intent(PROGRESS_UPDATE)
        intent.putExtra("downloadComplete", downloadComplete)
        LocalBroadcastManager.getInstance(this@DownloadNotificationService).sendBroadcast(intent)
    }

    private fun onDownloadComplete(downloadComplete: Boolean) {
        sendProgressUpdate(downloadComplete)
        notificationManager!!.cancel(0)
        notificationBuilder!!.setProgress(0, 0, false)
        notificationBuilder!!.setContentText("Image Download Complete")
        notificationManager!!.notify(0, notificationBuilder!!.build())
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        notificationManager!!.cancel(0)
    }

}