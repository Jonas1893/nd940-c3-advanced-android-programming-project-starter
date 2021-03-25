package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        Timber.plant(Timber.DebugTree())

        setupNotifications()

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            if (radio_group.checkedRadioButtonId == -1) {
                Toast.makeText(applicationContext,
                        getString(R.string.toast_select_file_to_download),
                        Toast.LENGTH_SHORT).show()
            } else {
                download()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        unregisterReceiver(receiver)
    }

    private fun setupNotifications() {
        notificationManager = getSystemNotificationManager()

        createChannel(
            getString(R.string.download_notification_channel_id),
            getString(R.string.download_notification_channel_name)
        )
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            custom_button.buttonState = ButtonState.Completed

            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: return
            val downloadStatus = mapDownloadStatus(id) ?: return

            val radioGroup = findViewById<RadioGroup>(R.id.radio_group)
            val checkedRadioButton = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)

            if (id == downloadID && mapDownloadStatus(id) != null && checkedRadioButton != null) {

                sendToast(downloadStatus)

                val intent = notificationManager.buildPendingIntent(applicationContext,
                    downloadStatus,
                    checkedRadioButton.text.toString()
                )
                pendingIntent = intent
                action = notificationManager.buildAction(applicationContext, pendingIntent)
                notificationManager.sendNotification(pendingIntent, application.getString(R.string.notification_description), application)
            }
        }
    }

    private fun sendToast(downloadStatus: DownloadStatus) {
        if (downloadStatus == DownloadStatus.COMPLETED) {
            Toast.makeText(applicationContext, getString(R.string.toast_download_success), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, getString(R.string.toast_download_failure), Toast.LENGTH_SHORT).show()
        }
    }

    private fun mapDownloadStatus(id: Long) : DownloadStatus? {
        val query = DownloadManager.Query()
        query.setFilterById(id)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val cursor = downloadManager.query(query)

        if (cursor.moveToFirst()){
            when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    return DownloadStatus.COMPLETED
                }
                DownloadManager.STATUS_FAILED -> {
                    return DownloadStatus.FAILED
                }
            }
        }

        return null
    }

    private fun download() {
        Timber.d("Starting Request")

        custom_button.buttonState = ButtonState.Loading

        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_description)

            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

    private fun getSystemNotificationManager(): NotificationManager {
        return ContextCompat.getSystemService(
            application,
            NotificationManager::class.java
        ) as NotificationManager
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
