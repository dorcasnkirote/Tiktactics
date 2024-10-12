package com.kamilimu.tiktaktics.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "emails")
data class EmailEntity(
    @PrimaryKey(autoGenerate = false)
    val messageId: String,
    val from: String,
    val subject: String,
    val sentDate: String,
    val content: String
)
