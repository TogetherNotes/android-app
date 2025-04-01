package com.example.togethernotes.models

import java.util.Date

 data class ArtistGenre(
    val artist_id : Int = 0,
    val genre_id : Int = 0,
    val creationDate : Date = Date()
                       )