package com.example.playlistmaker

import android.content.Intent
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

        val searchClickListener: View.OnClickListener = View.OnClickListener {
            val shareIntent = Intent(this, SearchActivity::class.java)
            startActivity(shareIntent)
        }

        search.setOnClickListener(searchClickListener)

        val mediateka = findViewById<Button>(R.id.mediateka_btn)

        mediateka.setOnClickListener {
            val mediatekaIntent = Intent(this, MediatekaActivity::class.java)
            startActivity(mediatekaIntent)
        }

        val settings = findViewById<Button>(R.id.settings_btn)

        settings.setOnClickListener{
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}