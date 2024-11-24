package com.example.playlistmaker

import android.R.array
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val PRACTICUM_EXAMPLE_PREFERENCES = "user_preferences"
const val TRACKS_LIST_KEY = "key_for_tracks_list"

class SearchActivity : AppCompatActivity(), TrackAdapter.Listener {

    private var countValue: String = ""
    private var trackCounter: Int = 0

    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var clearHistoryButton: Button
    private lateinit var emptySearchFrame: LinearLayout
    private lateinit var troubleWithConnectionFrame: LinearLayout
    private lateinit var tracksHistoryFrameLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var refreshButton: Button
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var progressBar: ProgressBar

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ITunesApi::class.java)

    val trackList: MutableList<Track> = ArrayList()
    var prefTrackList: MutableList<Track> = MutableList(15) { Track() }
    var correctPrefList: MutableList<Track> = MutableList(15) { Track() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        prefTrackList.clear()
        correctPrefList.clear()

        val sharedPreferences = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)

        val previousTrackList = sharedPreferences.getString(TRACKS_LIST_KEY, null)
            ?.let { createTrackFromJson(it) }

        if (previousTrackList != null) {
            correctPrefList = previousTrackList.toMutableList()
        }

        inputEditText = findViewById(R.id.searchEditText)
        progressBar = findViewById(R.id.progressBar)
        clearButton = findViewById(R.id.clear_btn)
        clearHistoryButton = findViewById(R.id.clear_history_btn)
        backButton = findViewById(R.id.back_btn)
        emptySearchFrame = findViewById(R.id.emptySearchFrameLayout)
        troubleWithConnectionFrame = findViewById(R.id.troubleWithConnectionFrameLayout)
        tracksHistoryFrameLayout = findViewById(R.id.tracksHistoryFrameLayout)
        recyclerView = findViewById(R.id.recyclerView)
        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        refreshButton = findViewById(R.id.refresh_btn)

        recyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)

        trackAdapter = TrackAdapter(trackList, this)
        recyclerView.adapter = trackAdapter

        historyAdapter = TrackAdapter(correctPrefList, this)
        historyRecyclerView.adapter = historyAdapter

        inputEditText.setOnFocusChangeListener{ view, hasFocus ->
            historyRecyclerView.visibility = if(inputEditText.hasFocus() && inputEditText.text.isEmpty() && correctPrefList.isNotEmpty()) View.VISIBLE else View.GONE
            tracksHistoryFrameLayout.visibility = if(inputEditText.hasFocus() && inputEditText.text.isEmpty() && correctPrefList.isNotEmpty()) View.VISIBLE else View.GONE
        }

        val clearButtonWatcher = object : TextWatcher, TrackAdapter.Listener {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //override
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(p0)
                recyclerView.visibility = clearButtonVisibility(p0)

                emptySearchFrame.visibility = View.GONE
                troubleWithConnectionFrame.visibility = View.GONE

                searchDebounce()

                val previousTrackList = sharedPreferences.getString(TRACKS_LIST_KEY, null)
                    ?.let { createTrackFromJson(it) }

                if (previousTrackList != null) {
                    correctPrefList = previousTrackList.toMutableList()
                }
                historyAdapter = TrackAdapter(correctPrefList, this)
                historyRecyclerView.adapter = historyAdapter

                historyRecyclerView.visibility = if(inputEditText.hasFocus() && p0?.isEmpty() == true && correctPrefList.isNotEmpty()) View.VISIBLE else View.GONE
                tracksHistoryFrameLayout.visibility = if(inputEditText.hasFocus() && p0?.isEmpty() == true && correctPrefList.isNotEmpty()) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(p0: Editable?) {
                countValue = p0.toString()
            }

            override fun onClick(track: Track) {
                TODO("Not yet implemented")
            }
        }

        inputEditText.addTextChangedListener(clearButtonWatcher)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            trackList.clear()
            correctPrefList.clear()
            val previousTrackList = sharedPreferences.getString(TRACKS_LIST_KEY, null)
                ?.let { createTrackFromJson(it) }

            if (previousTrackList != null) {
                correctPrefList = previousTrackList.toMutableList()
            }
            historyAdapter = TrackAdapter(correctPrefList, this)
            historyRecyclerView.adapter = historyAdapter

            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                clearButton.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            historyRecyclerView.visibility = if(inputEditText.text?.isEmpty() == true && correctPrefList.isNotEmpty()) View.VISIBLE else View.GONE
            tracksHistoryFrameLayout.visibility = if(inputEditText.text?.isEmpty() == true && correctPrefList.isNotEmpty()) View.VISIBLE else View.GONE
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (inputEditText.text.isNotEmpty()) {
                    getTracks()
                }
                true
            }
            false
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        refreshButton.setOnClickListener {
            getTracks()
        }

        clearHistoryButton.setOnClickListener {
            prefTrackList.clear()
            correctPrefList.clear()
            sharedPreferences.edit()
                .putString(TRACKS_LIST_KEY, createJsonFromTrack(correctPrefList))
                .apply()
            historyAdapter.notifyDataSetChanged()
            historyRecyclerView.visibility = View.GONE
            tracksHistoryFrameLayout.visibility = View.GONE
        }

    }

    override fun onStop() {
        super.onStop()

        val sharedPreferences = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(TRACKS_LIST_KEY, createJsonFromTrack(correctPrefList))
            .apply()
        prefTrackList.clear()
        correctPrefList.clear()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PRODUCT_AMOUNT, countValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Вторым параметром мы передаём значение по умолчанию
        countValue = savedInstanceState.getString(PRODUCT_AMOUNT, AMOUNT_DEF)
        inputEditText.setText(countValue)
    }

    companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
        const val AMOUNT_DEF = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun getTracks() {
        if (inputEditText.text.isNotEmpty()) {

            recyclerView.visibility = View.GONE
            troubleWithConnectionFrame.visibility = View.GONE
            emptySearchFrame.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            itunesService.search(inputEditText.text.toString()).enqueue(object :
                Callback<TrackResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    refreshTrackFrame()

                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            progressBar.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                            trackList.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            progressBar.visibility = View.GONE
                            trackAdapter.notifyDataSetChanged()
                            emptySearchFrame.visibility = View.VISIBLE
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    refreshTrackFrame()
                    trackAdapter.notifyDataSetChanged()
                    troubleWithConnectionFrame.visibility = View.VISIBLE
                }

            })
        }
    }

    private fun refreshTrackFrame(){
        troubleWithConnectionFrame.visibility = View.GONE
        emptySearchFrame.visibility = View.GONE
        trackList.clear()
        trackAdapter.notifyDataSetChanged()
        historyAdapter.notifyDataSetChanged()
        recyclerView.invalidate()
        historyRecyclerView.invalidate()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish() // Завершаем текущее Activity
    }

    override fun onClick(track: Track) {
        AddTrackInHistory(track)
        val trackIntent = Intent(this, TrackActivity::class.java)
        trackIntent.putExtra("trackName", track.trackName)
        trackIntent.putExtra("trackDuration", track.trackTimeMillis)
        trackIntent.putExtra("artistName", track.artistName)
        trackIntent.putExtra("collectionName", track.collectionName)
        trackIntent.putExtra("releaseDate", track.releaseDate)
        trackIntent.putExtra("primaryGenreName", track.primaryGenreName)
        trackIntent.putExtra("country", track.country)
        trackIntent.putExtra("icon", track.getCoverArtwork())
        startActivity(trackIntent)
    }

    private fun AddTrackInHistory(track: Track){
        val sharedPreferences = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        val previousTrackList = sharedPreferences.getString(TRACKS_LIST_KEY, null)
            ?.let { createTrackFromJson(it) }

        if (previousTrackList != null) {
            prefTrackList = previousTrackList.toMutableList()
        }
        if(prefTrackList.size == 10)
            prefTrackList.removeAt(9)
        prefTrackList.reverse()
        prefTrackList.remove(track)
        prefTrackList.add(track)
        prefTrackList.reverse()
        correctPrefList = prefTrackList
        sharedPreferences.edit()
            .putString(TRACKS_LIST_KEY, createJsonFromTrack(correctPrefList))
            .apply()
    }

    private fun createJsonFromTrack(track: List<Track>): String{
        return Gson().toJson(track)
    }

    private fun createTrackFromJson(json: String) : Array<Track>{
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { getTracks() }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}