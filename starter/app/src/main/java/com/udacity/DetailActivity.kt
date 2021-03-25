package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var fileNameTextView: TextView
    private lateinit var okButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        statusTextView = findViewById(R.id.statusTextView)
        fileNameTextView = findViewById(R.id.fileNameTextView)
        okButton = findViewById(R.id.confirmButton)

        statusTextView.text = intent.getStringExtra("DOWNLOAD_STATUS")
        fileNameTextView.text = intent.getStringExtra("FILE_NAME")

        okButton.setOnClickListener {
            finish()
        }

        val notificationManager = getSystemNotificationManager()
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun getSystemNotificationManager(): NotificationManager {
        return ContextCompat.getSystemService(
                application,
                NotificationManager::class.java
        ) as NotificationManager
    }

}
