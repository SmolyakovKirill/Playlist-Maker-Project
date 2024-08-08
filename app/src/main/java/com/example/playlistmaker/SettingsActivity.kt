package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch

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

        val supportButton = findViewById<ImageButton>(R.id.support_btn)

        val supportClickListener: View.OnClickListener = View.OnClickListener {
            val emailSend = "smolyakov.kirusha@gmail.com"
            val emailSubject = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val emailBody = "Спасибо разработчикам и разработчицам за крутое приложение!"

            val intent = Intent(Intent.ACTION_SEND)

            intent.putExtra(Intent.EXTRA_EMAIL, emailSend)
            intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            intent.putExtra(Intent.EXTRA_TEXT, emailBody)

            intent.type = "message/rfc822"

            startActivity(Intent.createChooser(intent, "Email to developer"))
        }
        supportButton.setOnClickListener(supportClickListener)

        val termsOfUse = findViewById<ImageButton>(R.id.term_of_use_btn)

        val termsOfUseListener: View.OnClickListener = View.OnClickListener {
            val url = "https://yandex.ru/legal/practicum_offer/"
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
            startActivity(urlIntent)
        }
        termsOfUse.setOnClickListener(termsOfUseListener)

        val backButton = findViewById<ImageButton>(R.id.back_btn)

        backButton.setOnClickListener{
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

    }
}