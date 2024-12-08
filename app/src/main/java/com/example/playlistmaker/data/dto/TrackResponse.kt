package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.Track

class TrackResponse(
    val searchType: String,
    val expression: String,
    val results: List<TrackDto>
) : Response()