package com.example.togethernotes.models

import java.util.Date

data class Chat(
    val id: Int = 0,
    val date: Date = Date(), // Fecha actual por defecto
    val user1_id: Int = 0,
    val user2_id: Int = 0
               )