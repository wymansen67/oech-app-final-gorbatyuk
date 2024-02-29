package com.example.oche_app_final.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.oche_app_final.R
import com.example.oche_app_final.databinding.ActivityOnboardingScreen2Binding

class OnboardingScreen2 : AppCompatActivity() {

    lateinit var binding: ActivityOnboardingScreen2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingScreen2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setTheme(R.style.Theme_Ocheappfinal)

        binding.nextButton.setOnClickListener {
            val intent = Intent(this, OnboardingScreen3::class.java)
            startActivity(intent)
            finish()
        }

        binding.skipButton.setOnClickListener {
            val intent = Intent(this, OnboardingScreen3::class.java)
            startActivity(intent)
            finish()
        }
    }
}