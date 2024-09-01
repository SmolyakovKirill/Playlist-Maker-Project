package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity(), TrackAdapter.Listener {

    private var countValue: String = ""

    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var emptySearchFrame: LinearLayout
    private lateinit var troubleWithConnectionFrame: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshButton: Button
    private lateinit var trackAdapter: TrackAdapter

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ITunesApi::class.java)

    val trackList: MutableList<Track> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputEditText = findViewById(R.id.searchEditText)
        clearButton = findViewById(R.id.clear_btn)
        backButton = findViewById(R.id.back_btn)
        emptySearchFrame = findViewById(R.id.emptySearchFrameLayout)
        troubleWithConnectionFrame = findViewById(R.id.troubleWithConnectionFrameLayout)
        recyclerView = findViewById(R.id.recyclerView)
        refreshButton = findViewById(R.id.refresh_btn)

        recyclerView.layoutManager = LinearLayoutManager(this)

        trackAdapter = TrackAdapter(trackList, this)
        recyclerView.adapter = trackAdapter

        val clearButtonWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //empty
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(p0)
                recyclerView.visibility = clearButtonVisibility(p0)
                emptySearchFrame.visibility = View.GONE
                troubleWithConnectionFrame.visibility = View.GONE
            }

            override fun afterTextChanged(p0: Editable?) {
                countValue = p0.toString()
            }
        }

        inputEditText.addTextChangedListener(clearButtonWatcher)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            trackList.clear()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                clearButton.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
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
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun getTracks() {
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
                        trackList.addAll(response.body()?.results!!)
                        trackAdapter.notifyDataSetChanged()
                    }
                    if (trackList.isEmpty()) {
                        trackAdapter.notifyDataSetChanged()
                        emptySearchFrame.visibility = View.VISIBLE
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                refreshTrackFrame()
                trackAdapter.notifyDataSetChanged()
                troubleWithConnectionFrame.visibility = View.VISIBLE
            }

        })
    }

    private fun refreshTrackFrame(){
        troubleWithConnectionFrame.visibility = View.GONE
        emptySearchFrame.visibility = View.GONE
        trackList.clear()
        trackAdapter.notifyDataSetChanged()
        recyclerView.invalidate()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish() // Завершаем текущее Activity
    }

    override fun onClick(track: Track) {
        Toast.makeText(this, track.trackName, Toast.LENGTH_LONG).show()
    }
}