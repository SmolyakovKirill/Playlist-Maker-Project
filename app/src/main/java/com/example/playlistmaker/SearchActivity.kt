package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SearchActivity : AppCompatActivity() {

    private var countValue: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val inputEditText = findViewById<EditText>(R.id.searchEditText)
        val clearButton = findViewById<ImageButton>(R.id.clear_btn)
        val backButton = findViewById<ImageButton>(R.id.back_btn)

        val trackList: MutableList<Track> = ArrayList()

        val nirvanaTrack: Track = Track()
        val jacksonTrack: Track = Track()
        val beeTrack: Track = Track()
        val zeppelinTrack: Track = Track()
        val gunsTrack: Track = Track()

        trackList.add(nirvanaTrack)
        trackList.add(jacksonTrack)
        trackList.add(beeTrack)
        trackList.add(zeppelinTrack)
        trackList.add(gunsTrack)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val trackAdapter = TrackAdapter(trackList)
        recyclerView.adapter = trackAdapter

        val clearButtonWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //empty
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(p0)
                recyclerView.visibility = clearButtonVisibility(p0)
            }

            override fun afterTextChanged(p0: Editable?) {
                countValue = p0.toString()
            }
        }

        inputEditText.addTextChangedListener(clearButtonWatcher)

        clearButton.setOnClickListener{
            inputEditText.setText("")

            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                clearButton.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

        backButton.setOnClickListener{
            onBackPressed()
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

        val inputEditText = findViewById<EditText>(R.id.searchEditText)
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish() // Завершаем текущее Activity
    }
}