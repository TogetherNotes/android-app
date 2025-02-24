package com.example.togethernotes.models

data class Schedule(
    val CreatedAt: String,
    val MusicianID: Int,
    val PerformanceDate: String,
    val ScheduleID: Int,
    val Status: String,
    val VenueID: Int
)