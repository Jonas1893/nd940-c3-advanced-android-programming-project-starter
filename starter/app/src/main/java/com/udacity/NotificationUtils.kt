package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

val NOTIFICATION_ID = 0


fun NotificationManager.sendNotification(contentPendingIntent: PendingIntent, messageBody: String, applicationContext: Context) {

    // let's cancel all pending notifications each time we schedule a new notification
    cancelNotifications()

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.download_notification_channel_id)
    )

    builder.setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(
                R.drawable.ic_assistant_black_24dp,
                applicationContext.getString(R.string.notification_button),
                contentPendingIntent
            )

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.buildPendingIntent(applicationContext: Context,
                                           downloadStatus: DownloadStatus,
                                           fileName: String): PendingIntent {
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra("DOWNLOAD_STATUS", downloadStatus.toString())
    contentIntent.putExtra("FILE_NAME", fileName)

    return PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}

fun NotificationManager.buildAction(applicationContext: Context, pendingIntent: PendingIntent) = NotificationCompat.Action(
    R.drawable.ic_assistant_black_24dp,
    applicationContext.getString(R.string.notification_button),
    pendingIntent
)

private fun NotificationManager.cancelNotifications() {
    cancelAll()
}

