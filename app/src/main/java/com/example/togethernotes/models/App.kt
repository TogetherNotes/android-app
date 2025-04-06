package com.example.togethernotes.models

open class App(
    val id: Int = 0,
    val name: String = "",
    var mail: String = "",
    var password: String = "",
    val role: String = "",
    val rating: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val active: Boolean = true,
    val language_id: Int = 1
              )
