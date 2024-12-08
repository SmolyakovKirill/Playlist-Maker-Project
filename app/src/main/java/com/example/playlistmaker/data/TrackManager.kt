package com.example.playlistmaker.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson

const val THEME_PREFERENCES = "theme_preferences"

class TrackManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
    private val gson = Gson()
}