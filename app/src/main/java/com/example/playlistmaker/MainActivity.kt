package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val search = findViewById<Button>(R.id.search_btn)

        val searchClickListener: View.OnClickListener = View.OnClickListener { Toast.makeText(this@MainActivity, "Нажали на кнопку поиска!", Toast.LENGTH_SHORT).show() }

        search.setOnClickListener(searchClickListener)

        val mediateka = findViewById<Button>(R.id.mediateka_btn)

        mediateka.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку медиатеки!", Toast.LENGTH_SHORT).show()
        }

        val settings = findViewById<Button>(R.id.settings_btn)

        settings.setOnClickListener{
            Toast.makeText(this@MainActivity, "Нажали на кнопку настроек!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}