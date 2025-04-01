package com.example.togethernotes.models

class Artist(
    active: Boolean = false,
    id: Int = 0,
    language_id: Int = 0,
    latitude: Double = 0.0,
    longitude: Double = 0.0,
    mail: String = "",
    name: String = "",
    password: String = "",
    rating: Double = 0.0,
    role: String = "",

    val app_user_id: Int = 0,
            ) : App(id, name, mail, password, role, rating, latitude, longitude, active, language_id)
