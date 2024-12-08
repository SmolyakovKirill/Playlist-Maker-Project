package com.example.playlistmaker.ui.tracks

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.tracks.TrackAdapter.Listener
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView){

    private val sourceName: TextView = parentView.findViewById(R.id.sourceName)
    private val text: TextView = parentView.findViewById(R.id.text)
    private val trackIcon: ImageView = parentView.findViewById(R.id.trackIcon)
    private val trackTime: TextView = parentView.findViewById(R.id.trackTime)

    fun bind(model: Track, listener: Listener) {
        // присваиваем в TextView значения из нашей модели
        val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        sourceName.text = model.trackName
        text.text = model.artistName
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(10))
            .into(trackIcon)
        trackTime.text = dateFormatter.format(model.trackTimeMillis)
        
        itemView.setOnClickListener {
            listener.onClick(model)
        }
    }
}