package com.example.togethernotes.models

data class TempMatch(
    val artist_id: Int,
    val space_id: Int,
    val artist_like: Boolean?,
    val space_like: Boolean?,
    val status: String?,
    val request_date: String? // Puedes usar un tipo más específico como `Date` si lo deseas
)