package com.example.togethernotes.models

data class Match(
    val artist_id: Int,
    val space_id: Int,
    val match_date: String // Ajusta el tipo según el formato de fecha devuelto por la API
                )