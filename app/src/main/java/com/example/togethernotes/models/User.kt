package com.example.togethernotes.models


open class User(
    val active: Boolean,
    val file_id: Int,
    val id: Int,
    val language_id: Int,
    val latitude: Double,
    val longitude: Double,
    var mail: String,
    val name: String,
    val notification_id: Int,
    var password: String,
    val rating: Int,
    var role: String
)