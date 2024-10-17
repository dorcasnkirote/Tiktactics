package com.example.tiktacticssignup_login.data.remote

import com.example.tiktacticssignup_login.data.remote.dtos.request.EmailsDto
import com.example.tiktacticssignup_login.data.remote.dtos.request.LoginRequestDto
import com.example.tiktacticssignup_login.data.remote.dtos.request.RefreshTokenRequestDto
import com.example.tiktacticssignup_login.data.remote.dtos.request.RegistrationDto
import com.example.tiktacticssignup_login.data.remote.dtos.response.LoginResponseDto
import com.example.tiktacticssignup_login.data.remote.dtos.response.RefreshTokenResponseDto
import com.example.tiktacticssignup_login.data.remote.dtos.response.RegistrationResponse
import com.example.tiktacticssignup_login.data.remote.dtos.response.SpamEmailsDto
import okhttp3.Interceptor
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

    @POST("start-detection/")
    suspend fun startDetection(
        @Body emails: EmailsDto
    ): SpamEmailsDto

    @POST("api/token/refresh/")
    suspend fun refreshToken(
        @Body refreshTokenRequestDto: RefreshTokenRequestDto
    ): RefreshTokenResponseDto


    companion object {
        private fun httpLoggingInterceptor() = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        private fun okhttpClient(authToken: String? = null): OkHttpClient {
            return if (authToken != null) OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor())
                .addInterceptor(authInterceptor(authToken))
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()
            else OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor())
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()
        }

        private fun authInterceptor(token: String) = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }


        fun getInstance(authToken: String? = null): TiktacticsApiService {
            return Retrofit.Builder()
                .baseUrl("https://tiktactics.onrender.com/api/v1/")
                .client(okhttpClient(authToken))
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .build()
                .create(TiktacticsApiService::class.java)
        }
    }
}

