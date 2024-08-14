package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class TracksViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView){
    private val sourceName: TextView = parentView.findViewById(R.id.sourceName)
    private val text: TextView = parentView.findViewById(R.id.text)
    private val trackIcon: ImageView = parentView.findViewById(R.id.trackIcon)
    private val trackTime: TextView = parentView.findViewById(R.id.trackTime)

    fun bind(model: Track) {
        // присваиваем в TextView значения из нашей модели
        sourceName.text = model.trackName
        text.text = model.artistName
        Glide.with(itemView).load(model.artworkUrl100).into(trackIcon)
        trackTime.text = model.trackTime
    }
}