package com.example.tiktacticssignup_login.data.remote

import com.example.tiktacticssignup_login.data.remote.dtos.request.LoginRequestDto
import com.example.tiktacticssignup_login.data.remote.dtos.request.RegistrationDto
import com.example.tiktacticssignup_login.data.remote.dtos.response.LoginResponseDto
import com.example.tiktacticssignup_login.data.remote.dtos.response.RegistrationResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface TiktacticsApiService {

    @POST("auth/register/")
    suspend fun registerUser(
        @Body registrationDto: RegistrationDto
    ): RegistrationResponse

    @POST("auth/login/")
    suspend fun loginUser(
        @Body loginRequestDto: LoginRequestDto
    ): LoginResponseDto


    companion object {
        private fun httpLoggingInterceptor() =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        private fun okhttpClient() = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor())
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()


        fun getInstance(): TiktacticsApiService {
            return Retrofit.Builder()
                .baseUrl("https://tiktactics.onrender.com/api/v1/")
                .client(okhttpClient())
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .build()
                .create(TiktacticsApiService::class.java)
        }
    }
}

