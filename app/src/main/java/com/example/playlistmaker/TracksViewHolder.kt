package com.example.playlistmaker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TracksViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView){
    private val sourceName: TextView = parentView.findViewById(R.id.sourceName)
    private val text: TextView = parentView.findViewById(R.id.text)

    fun bind(model: Track) {
        // присваиваем в TextView значения из нашей модели
        sourceName.text = model.trackName
        text.text = model.artistName
    }
}