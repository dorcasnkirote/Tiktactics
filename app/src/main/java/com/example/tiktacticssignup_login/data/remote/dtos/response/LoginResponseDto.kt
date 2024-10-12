package com.example.tiktacticssignup_login.data.remote.dtos.response


import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("access")
    val access: String,
    @SerializedName("detail")
    val detail: String,
    @SerializedName("refresh")
    val refresh: String
)