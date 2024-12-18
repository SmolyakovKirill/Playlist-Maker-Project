package com.example.playlistmaker

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class SettingsActivity : AppCompatActivity() {

    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeSwitcher = findViewById<Switch>(R.id.switchTheme_btn)
        sharedPreferences = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
        val themePref = sharedPreferences?.getString(THEME_PREFERENCES, "")

        if (themePref == "DARK") {
            themeSwitcher.isChecked = true
        }

        val shareButton = findViewById<ImageButton>(R.id.share_btn)

        val shareClickListener: View.OnClickListener = View.OnClickListener {
            val shareData = Intent(Intent.ACTION_SEND)
            shareData.type = "text/plain"
            val dataToShare = this.getString(R.string.practicum_url)
            shareData.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.share_subject))
            shareData.putExtra(Intent.EXTRA_TEXT, dataToShare)
            startActivity(
                Intent.createChooser(
                    shareData,
                    this.getString(R.string.share_intent_title)
                )
            )
        }
        shareButton.setOnClickListener(shareClickListener)

        val supportButton = findViewById<ImageButton>(R.id.support_btn)

        val supportClickListener: View.OnClickListener = View.OnClickListener {
            val emailSend = this.getString(R.string.support_email)
            val emailSubject = this.getString(R.string.support_email_subject)
            val emailBody = this.getString(R.string.support_email_body)

            val selectorIntent = Intent(
                Intent.ACTION_SENDTO,
                Uri.parse("mailto:?subject=$emailSubject&to=$emailSend&body=$emailBody")
            )

            startActivity(selectorIntent)
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

        backButton.setOnClickListener {
            onBackPressed()
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            if (checked) {
                (applicationContext as App).sharedPreferences?.edit()
                    ?.putString(THEME_PREFERENCES, "DARK")?.apply()
            } else {
                (applicationContext as App).sharedPreferences?.edit()
                    ?.putString(THEME_PREFERENCES, "LIGHT")?.apply()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish() // Завершаем текущее Activity
    }


}