package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {

    private lateinit var songNameTextView: TextView
    private lateinit var groupNameTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var albumTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var trackIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val backButton = findViewById<ImageButton>(R.id.track_back_btn)

        val trackName = intent.getSerializableExtra("trackName")
        val groupName = intent.getSerializableExtra("artistName")
        val duration = intent.getSerializableExtra("trackDuration")
        val collectionName = intent.getSerializableExtra("collectionName")
        val releaseDate = intent.getSerializableExtra("releaseDate")
        val primaryGenreName = intent.getSerializableExtra("primaryGenreName")
        val country = intent.getSerializableExtra("country")
        val icon = intent.getSerializableExtra("icon")

        val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())

        trackIcon = findViewById(R.id.trackMediaIcon)

        songNameTextView = findViewById(R.id.song_name)
        songNameTextView.text = trackName.toString()

        groupNameTextView = findViewById(R.id.group_name)
        groupNameTextView.text = groupName.toString()

        durationTextView = findViewById(R.id.track_duration_value)
        durationTextView.text = dateFormatter.format(duration)

        albumTextView = findViewById(R.id.track_album_value)
        albumTextView.text = collectionName.toString()

        genreTextView = findViewById(R.id.track_genre_value)
        genreTextView.text = primaryGenreName.toString()

        yearTextView = findViewById(R.id.track_year_value)
        yearTextView.text = releaseDate.toString()

        countryTextView = findViewById(R.id.track_country_value)
        countryTextView.text = country.toString()


        Glide.with(baseContext)
            .load(icon)
            .placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(10))
            .into(trackIcon)

        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish() // Завершаем текущее Activity
    }
}