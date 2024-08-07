package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val shareButton = findViewById<ImageButton>(R.id.share_btn)

        val shareClickListener: View.OnClickListener = View.OnClickListener {
            val shareData = Intent(Intent.ACTION_SEND)
            shareData.type = "text/plain"
            val dataToShare = "https://practicum.yandex.ru/profile/android-developer/"
            shareData.putExtra(Intent.EXTRA_SUBJECT, "Link to YP lessons")
            shareData.putExtra(Intent.EXTRA_TEXT, dataToShare)
            startActivity(Intent.createChooser(shareData, "Share Link"))
        }
        shareButton.setOnClickListener(shareClickListener)
    }
}