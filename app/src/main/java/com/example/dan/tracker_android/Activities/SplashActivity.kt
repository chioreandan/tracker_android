package com.example.dan.tracker_android.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import com.example.dan.tracker_android.R
import java.util.*

class SplashActivity : AppCompatActivity() {

    val STR_SPLASH_TIME = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startSplashTimer()
    }

    private fun startSplashTimer() = try {
        val timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            },
            3000
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

