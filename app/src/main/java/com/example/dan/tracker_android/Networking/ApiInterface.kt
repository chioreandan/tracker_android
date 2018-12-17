package com.example.dan.tracker_android.Networking

import retrofit2.Call
import retrofit2.http.*
import com.example.dan.tracker_android.Models.*


interface ApiInterface {
    @POST("api/v1/sessions")
    fun loginCall(@Query("user[email]") email: String,
                  @Query("user[password]") password: String): Call<LoginResponse>

    @POST("api/v1/locations")
    fun postLocation(@Query("user_token") user_token: String,
                     @Query("user_email") user_email: String,
                     @Query("location[latitude]") latitude: Double,
                     @Query("location[longitude]") longitude: Double): Call<Locat>

}