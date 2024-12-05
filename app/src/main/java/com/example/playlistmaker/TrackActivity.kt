package com.example.playlistmaker

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
    }

    private var playerState = STATE_DEFAULT

    private lateinit var playButton: ImageButton
    private lateinit var trackProgress: TextView
    private var mediaPlayer = MediaPlayer()
    private var url: String? = ""
    private var mainThreadHandler: Handler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        mainThreadHandler = Handler(Looper.getMainLooper())

        val backButton = findViewById<ImageButton>(R.id.track_back_btn)
        playButton = findViewById(R.id.play_btn)
        trackProgress = findViewById(R.id.currentTrackProgess)

        val trackName = intent.getSerializableExtra(TRACK_NAME)
        val groupName = intent.getSerializableExtra(ARTIST_NAME)
        val duration = intent.getSerializableExtra(TRACK_DURATION)
        val collectionName = intent.getSerializableExtra(COLLECTION_NAME)
        val releaseDate = intent.getSerializableExtra(RELEASE_DATE)
        val primaryGenreName = intent.getSerializableExtra(PRIMARY_GENRE_NAME)
        val country = intent.getSerializableExtra(COUNTRY)
        val icon = intent.getSerializableExtra(ICON)
        url = intent.getSerializableExtra(PREVIEW_URL).toString()

        preparePlayer()

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
        yearTextView.text = releaseDate.toString().substringBefore('-')

        countryTextView = findViewById(R.id.track_country_value)
        countryTextView.text = country.toString()


        Glide.with(baseContext).load(icon).placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(10)).into(trackIcon)

        backButton.setOnClickListener {
            onBackPressed()
        }
        playButton.setOnClickListener {
            playbackControl()
            startTimer()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler?.removeCallbacks(createUpdateTrackTimer())
        mediaPlayer.release()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.play_btn)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause_btn_dark)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play_btn)
        playerState = STATE_PAUSED
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish() // Завершаем текущее Activity
    }

    private fun startTimer() {
        mainThreadHandler?.post(
            createUpdateTrackTimer()
        )
    }

    private fun createUpdateTrackTimer(): Runnable {
        return object : Runnable {
            @SuppressLint("DefaultLocale")
            override fun run() {
                when (playerState) {
                    STATE_PLAYING -> {
                        trackProgress.text = SimpleDateFormat(
                            "mm:ss", Locale.getDefault()
                        ).format(mediaPlayer.currentPosition)
                        mainThreadHandler?.postDelayed(this, DELAY)
                    }

                    STATE_PAUSED -> {
                        mainThreadHandler?.removeCallbacks(this)
                    }

                    STATE_PREPARED -> {
                        trackProgress.text =
                            SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
                    }
                }

            }
        }
    }
}
