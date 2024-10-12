package com.example.tiktacticssignup_login.data.remote

import com.example.tiktacticssignup_login.data.remote.dtos.request.RegistrationDto
import com.example.tiktacticssignup_login.data.remote.dtos.response.RegistrationResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface TiktacticsApiService {

    @POST("auth/register")
    suspend fun registerUser(
        @Body registrationDto: RegistrationDto
    ): RegistrationResponse


    companion object {
        fun getInstance(): TiktacticsApiService {
            return Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .build()
                .create(TiktacticsApiService::class.java)
        }
    }
}

