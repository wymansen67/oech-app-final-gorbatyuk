package com.example.oche_app_final.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.oche_app_final.R
import com.example.oche_app_final.databinding.ActivitySplashScreenBinding
import com.example.oche_app_final.model.Constances
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTheme(R.style.Theme_Ocheappfinal)

        val delayMillis = 2000L
        val handler = Handler()
        var sessionExists: Boolean
        handler.postDelayed({
            lifecycleScope.launch(Dispatchers.IO) {
                val session = supabase.auth.sessionManager.loadSession()
                sessionExists = session != null
                Log.d("session", sessionExists.toString())
                if (sessionExists) {
                    val intent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@SplashScreen, OnboardingScreen1::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }, delayMillis)
    }

    val supabase = createSupabaseClient(
        supabaseUrl = "https://pnrpvncrwrvbmdxcewoi.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBucnB2bmNyd3J2Ym1keGNld29pIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDc5OTQwNTksImV4cCI6MjAyMzU3MDA1OX0.WaEuOMeUQZu-PVdCAVe4MuNRlsGqreHbyDNHKSbF3RM"
    ) {
        install(Auth)
    }
}