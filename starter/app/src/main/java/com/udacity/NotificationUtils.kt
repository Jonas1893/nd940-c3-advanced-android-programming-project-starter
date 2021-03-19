package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private val NOTIFICATION_ID = 0


fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    // let's cancel all pending notifications each time we schedule a new notification
    cancelNotifications()

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.download_notification_channel_id)
    )

    val contentPendingIntent = buildPendingIntent(applicationContext)

    builder.setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            contentPendingIntent
        )


    notify(NOTIFICATION_ID, builder.build())
}

private fun buildPendingIntent(applicationContext: Context): PendingIntent? {
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    return PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}

private fun NotificationManager.cancelNotifications() {
    cancelAll()
}

