package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

data class Track(
    val trackName: String = "Undefined",
    val artistName: String = "Undefined",
    val trackTimeMillis: Long = 0,
    val artworkUrl100: String = "Undefined"
)
