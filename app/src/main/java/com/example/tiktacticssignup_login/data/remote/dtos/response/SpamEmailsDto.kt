package com.example.tiktacticssignup_login.data.remote.dtos.response


import com.google.gson.annotations.SerializedName

data class SpamEmailsDto(
    @SerializedName("spam_emails")
    val spamEmailDtos: List<SpamEmailDto>
)