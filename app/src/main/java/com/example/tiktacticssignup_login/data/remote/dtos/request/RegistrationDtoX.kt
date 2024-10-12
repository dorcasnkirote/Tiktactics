package com.example.tiktacticssignup_login.data.remote.dtos.request


import com.google.gson.annotations.SerializedName

data class RegistrationDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("password1")
    val password1: String,
    @SerializedName("password2")
    val password2: String,
    @SerializedName("username")
    val username: String
)