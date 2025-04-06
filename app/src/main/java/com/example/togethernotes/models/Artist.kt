package com.example.togethernotes.models

class Artist(
    name: String = "",
    mail: String = "",
    password: String = "",
    role: String = "",
    rating: Int = 0,
    latitude: Double = 0.0,
    longitude: Double = 0.0,
    active: Boolean = true,
    language_id: Int = 1,
    id : Int = 0,
    var genre_ids: List<Int>,
            ) : App(id, name, mail, password, role, rating, latitude, longitude, active, language_id)
