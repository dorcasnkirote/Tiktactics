package com.example.tiktacticssignup_login.data.remote.dtos.response


import com.google.gson.annotations.SerializedName

data class RefreshTokenResponseDto(
    @SerializedName("access")
    val access: String,
    @SerializedName("refresh")
    val refresh: String
)