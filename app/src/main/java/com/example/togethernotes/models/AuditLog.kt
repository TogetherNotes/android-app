package com.example.togethernotes.models


data class AuditLog(
    val ActionType: String,
    val AuditID: Int,
    val Description: String,
    val RecordID: Int,
    val TableName: String,
    val Timestamp: String,
    val UserID: Int
)