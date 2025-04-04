// Chat.kt
package com.example.togethernotes.models

import java.util.Date

data class Chat(
    val id: Int,
    val date: Date,
    val user1_id: Int,
    val user2_id: Int,
    var userName: String? = null // Esta propiedad se añadirá temporalmente
               )
