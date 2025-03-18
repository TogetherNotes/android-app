package com.example.togethernotes.models


data class Activity(
    val artist_id: Int,
    val end_hour: String,
    val init_hour: String,
    val space_id: Int,
    val work: WorkType,
    val imagen: Int = 0, // Valor predeterminado
    val titulo: String = "", // Valor predeterminado
    val tipo: String = "" // Valor predeterminado
)

