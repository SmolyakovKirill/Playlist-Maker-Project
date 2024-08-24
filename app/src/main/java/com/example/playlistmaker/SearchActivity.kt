package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private var countValue: String = ""

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val trackList: MutableList<Track> = ArrayList()

    private val itunesService = retrofit.create(ITunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val inputEditText = findViewById<EditText>(R.id.searchEditText)
        val clearButton = findViewById<ImageButton>(R.id.clear_btn)
        val backButton = findViewById<ImageButton>(R.id.back_btn)
        val emptySearchFrame = findViewById<LinearLayout>(R.id.emptySearchFrameLayout)
        val troubleWithConnectionFrame = findViewById<LinearLayout>(R.id.troubleWithConnectionFrameLayout)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val refreshButton = findViewById<Button>(R.id.refresh_btn)

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
                emptySearchFrame.visibility = View.GONE
                troubleWithConnectionFrame.visibility = View.GONE
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

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (inputEditText.text.isNotEmpty()){
                    itunesService.search(inputEditText.text.toString()).enqueue(object :
                        Callback<TrackResponse>{
                        override fun onResponse(call: Call<TrackResponse>,
                                                response: Response<TrackResponse>
                        ) {
                            if (response.code() == 200) {
                                trackList.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    trackList.addAll(response.body()?.results!!)
                                    trackAdapter.notifyDataSetChanged()
                                }
                                if (trackList.isEmpty()){
                                    trackAdapter.notifyDataSetChanged()
                                    emptySearchFrame.visibility = View.VISIBLE
                                }
                            }
                            else{

                            }
                        }

                        override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                            trackAdapter.notifyDataSetChanged()
                            troubleWithConnectionFrame.visibility = View.VISIBLE
                        }

                    })
                }
                true
            }
            false
        }

        backButton.setOnClickListener{
            onBackPressed()
        }

        refreshButton.setOnClickListener {

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