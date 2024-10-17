package com.example.tiktacticssignup_login.data.remote.dtos.request


import com.google.gson.annotations.SerializedName

data class EmailDto(
    @SerializedName("body")
    val body: String,
    @SerializedName("from")
    val from: String,
    @SerializedName("messageID")
    val messageID: String,
    @SerializedName("subject")
    val subject: String
)