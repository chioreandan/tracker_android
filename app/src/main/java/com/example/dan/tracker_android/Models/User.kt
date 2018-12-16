package com.example.dan.tracker_android.Models

import com.google.gson.annotations.SerializedName

data class User(@SerializedName("id") var id: Int,
                @SerializedName("email") var email: String,
                @SerializedName("authentication_token") var authentication_token: String
)

