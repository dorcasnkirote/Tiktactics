package com.example.tiktacticssignup_login.data.remote.dtos.request


import com.google.gson.annotations.SerializedName

data class EmailsDto(
    @SerializedName("emails")
    val emailDtos: List<EmailDto>
)