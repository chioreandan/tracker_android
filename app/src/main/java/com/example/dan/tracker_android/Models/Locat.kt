package com.example.dan.tracker_android.Models

import com.google.gson.annotations.SerializedName

data class Locat(@SerializedName("id") var id: Int,
                    @SerializedName("latitude") var latitude: Double,
                    @SerializedName("longitude") var longitude: Double)
