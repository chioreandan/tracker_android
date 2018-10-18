package com.example.dan.tracker_android.Activities

import android.annotation.SuppressLint
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.dan.tracker_android.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

//    @SuppressLint("MissingPermission")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        button2.setOnClickListener({
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                if(location!=null){
//                    this.textView.text= location.longitude.toString()
//                }
//                else{
//                    this.textView.text="nope"
//                }
//            }
//        })
    //}
}
