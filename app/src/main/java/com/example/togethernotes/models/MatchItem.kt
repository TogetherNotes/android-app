package com.example.togethernotes.models

data class MatchItem(
    val imageUrl: String,
    val name: String,
    val description: String,
    val tempMatch: TempMatchDto
)