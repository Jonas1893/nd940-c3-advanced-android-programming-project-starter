package com.udacity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var fileNameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        statusTextView = findViewById(R.id.statusTextView)
        fileNameTextView = findViewById(R.id.fileNameTextView)

        statusTextView.text = intent.getStringExtra("DOWNLOAD_STATUS")
        fileNameTextView.text = intent.getStringExtra("FILE_NAME")
    }

}
