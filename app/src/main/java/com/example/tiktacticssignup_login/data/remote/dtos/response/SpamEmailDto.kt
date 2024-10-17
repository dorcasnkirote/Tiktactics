package com.example.tiktacticssignup_login.data.remote.dtos.response


import com.example.tiktacticssignup_login.data.local.entities.SpamEmailEntity
import com.google.gson.annotations.SerializedName

data class SpamEmailDto(
    @SerializedName("body")
    val body: String,
    @SerializedName("detected_on")
    val detectedOn: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("sender")
    val sender: String,
    @SerializedName("subject")
    val subject: String
){
    fun toSpamEmailEntity(): SpamEmailEntity {
        return SpamEmailEntity(
            id = id,
            body = body,
            detectedOn = detectedOn,
            messageId = messageId,
            sender = sender,
            subject = subject
        )
    }
}