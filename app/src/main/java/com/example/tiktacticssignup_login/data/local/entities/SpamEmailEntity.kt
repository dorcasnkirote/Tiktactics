package com.example.tiktacticssignup_login.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "spam_emails")
data class SpamEmailEntity(
    @PrimaryKey(autoGenerate = false)
    val messageId: String,
    val id: Int,
    val body: String,
    val detectedOn: String,
    val sender: String,
    val subject: String
)
