package com.example.togethernotes.models


data class Contract(
    val artist_id: Int,
    val space_id: Int,
    val meet_type: String,
    val status: String,
    val init_hour: String, // Considera usar un tipo más adecuado si es posible
    val end_hour: String,  // Considera usar un tipo más adecuado si es posible
    val title: String
)

