package com.example.tiktacticssignup_login.data.remote.dtos.request


import com.google.gson.annotations.SerializedName

data class RefreshTokenRequestDto(
    @SerializedName("refresh")
    val refresh: String
)