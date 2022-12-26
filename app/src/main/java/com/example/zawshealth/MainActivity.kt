package com.example.zawshealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.view.WindowManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            // on below line we are
            // creating a new intent
            val intent = Intent(
                this@MainActivity,
                MedicinesListActivity::class.java
            )
            // on below line we are
            // starting a new activity.
            startActivity(intent)

            // on the below line we are finishing
            // our current activity.
            finish()
        }, 1000)
    }
}