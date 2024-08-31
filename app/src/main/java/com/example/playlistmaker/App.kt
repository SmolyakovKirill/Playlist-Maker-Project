package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val THEME_PREFERENCES = "theme_preferences"

class App : Application() {
    private var darkTheme = true
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)

        val themePref = sharedPreferences?.getString(THEME_PREFERENCES,"")

        if(themePref == "LIGHT"){
            switchTheme(false)
        }
        else{
            switchTheme(true)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}