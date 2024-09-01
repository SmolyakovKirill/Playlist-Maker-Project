package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private val tracks: List<Track>, val listener: Listener
) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_view_design, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position], listener)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    interface Listener{
        fun onClick(track: Track)
    }

}