package com.example.playlistmaker

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PackageManagerCompat.LOG_TAG
import com.example.playlistmaker.SearchActivity.Companion.AMOUNT_DEF


var countValue: String = ""

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val inputEditText = findViewById<EditText>(R.id.searchEditText)
        val clearButton = findViewById<ImageButton>(R.id.clear_btn)
        val backButton = findViewById<ImageButton>(R.id.back_btn)

        val clearButtonWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //empty
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(p0)

            }

            override fun afterTextChanged(p0: Editable?) {
                countValue = p0.toString()
            }
        }

        inputEditText.addTextChangedListener(clearButtonWatcher)

        clearButton.setOnClickListener{
            inputEditText.setText("")
        }

        backButton.setOnClickListener{
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PRODUCT_AMOUNT, countValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val TAG = this.javaClass.simpleName
        super.onRestoreInstanceState(savedInstanceState)
        // Вторым параметром мы передаём значение по умолчанию
        countValue = savedInstanceState.getString(PRODUCT_AMOUNT, AMOUNT_DEF)

        val inputEditText = findViewById<EditText>(R.id.searchEditText)
        inputEditText.setText(countValue)
        Log.d(TAG, countValue)
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

}