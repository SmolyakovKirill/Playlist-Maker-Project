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
            val dataToShare = this.getString(R.string.practicum_url)
            shareData.putExtra(Intent.EXTRA_SUBJECT,  this.getString(R.string.share_subject))
            shareData.putExtra(Intent.EXTRA_TEXT, dataToShare)
            startActivity(Intent.createChooser(shareData,  this.getString(R.string.share_intent_title)))
        }
        shareButton.setOnClickListener(shareClickListener)

        val supportButton = findViewById<ImageButton>(R.id.support_btn)

        val supportClickListener: View.OnClickListener = View.OnClickListener {
            val emailSend = this.getString(R.string.support_email)
            val emailSubject = this.getString(R.string.support_email_subject)
            val emailBody = this.getString(R.string.support_email_body)

            val intent = Intent(Intent.ACTION_SEND)

            intent.putExtra(Intent.EXTRA_EMAIL, emailSend)
            intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            intent.putExtra(Intent.EXTRA_TEXT, emailBody)

            intent.type = "message/rfc822"

            startActivity(Intent.createChooser(intent, this.getString(R.string.email_intent_title)))
        }
        supportButton.setOnClickListener(supportClickListener)

        val termsOfUse = findViewById<ImageButton>(R.id.term_of_use_btn)

        val termsOfUseListener: View.OnClickListener = View.OnClickListener {
            val url = this.getString(R.string.agreement_url)
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
            startActivity(urlIntent)
        }
        termsOfUse.setOnClickListener(termsOfUseListener)

        val backButton = findViewById<ImageButton>(R.id.back_btn)

        backButton.setOnClickListener{
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish() // Завершаем текущее Activity
    }
}