package com.example.togethernotes.models


data class Performance(
    val CreatedAt: String,
    val MusicianID: Int,
    val PaymentAmount: Double,
    val PerformanceDate: String,
    val PerformanceFeedback: String,
    val PerformanceID: Int,
    val Rating: Double,
    val SetList: String,
    val VenueID: Int
)