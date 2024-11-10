package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

data class Track(
    val trackName: String = "Undefined",
    val artistName: String = "Undefined",
    val trackTimeMillis: Long = 0,
    val artworkUrl100: String = "Undefined",
    val collectionName: String = "Undefined",
    val releaseDate: String = "Undefined",
    val primaryGenreName: String = "Undefined",
    val country: String = "Undefined"
){
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}
