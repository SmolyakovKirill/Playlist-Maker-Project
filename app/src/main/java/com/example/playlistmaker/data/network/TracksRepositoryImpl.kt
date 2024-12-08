package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val trackNetworkClient: TrackNetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = trackNetworkClient.doRequest(TracksSearchRequest(expression))
        if(response.resultCode == 200){
            return (response as TrackResponse).results.map {
                Track(it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.collectionName, it.releaseDate, it.primaryGenreName, it.country, it.previewUrl)
            }
        } else {
            return emptyList()
        }
    }

}