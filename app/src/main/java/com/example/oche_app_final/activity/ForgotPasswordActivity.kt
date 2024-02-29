package com.example.oche_app_final.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.oche_app_final.R
import com.example.oche_app_final.databinding.ActivityForgotPasswordBinding
import com.example.oche_app_final.model.Constances
import com.google.android.material.snackbar.Snackbar
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTheme(R.style.Theme_Ocheappfinal)

        binding.signInTextView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.emailAddressEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val emailAddress: String = binding.emailAddressEditText.text.toString().trim()
                val isValidEmailAddress =
                    android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
                if (!isValidEmailAddress) {
                    binding.emailAddressEditText.error = "Enter a valid email address."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                getOtpButtonAccess(
                    binding.emailAddressEditText,
                    binding.sendOtpButton
                )
            }
        })

        binding.sendOtpButton.setOnClickListener {
            if (binding.emailAddressEditText.text.toString().isNotEmpty()) {
                val email = binding.emailAddressEditText.text.toString().lowercase()
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        supabase.auth.resetPasswordForEmail(email)
                        val intent =
                            Intent(this@ForgotPasswordActivity, OtpVerificationActivity::class.java)
                        intent.putExtra("emailAddr", email)
                        startActivity(intent)
                        finish()
                    } catch (error: Exception) {
                        Log.d("logInException", error.toString())
                        var snackbar =
                            Snackbar.make(it, R.string.something_wrong, Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                }
            } else {
                binding.emailAddressEditText.error = "Email cannot be empty"
            }
        }
    }

    val supabase = createSupabaseClient(
        supabaseUrl = "https://pnrpvncrwrvbmdxcewoi.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBucnB2bmNyd3J2Ym1keGNld29pIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDc5OTQwNTksImV4cCI6MjAyMzU3MDA1OX0.WaEuOMeUQZu-PVdCAVe4MuNRlsGqreHbyDNHKSbF3RM"
    ) {
        install(Auth)
        install(Postgrest)
    }

    private fun getOtpButtonAccess(
        emailAddressEditText: TextView,
        sendOtpButton: Button,
    ) {
        val emailCheck =
            !emailAddressEditText.text.isNullOrEmpty() && emailAddressEditText.error.isNullOrEmpty()

        if (emailCheck) {
            sendOtpButton.isClickable = true
            val color = Color.parseColor("#0560FA")
            sendOtpButton.setBackgroundColor(color)
        } else {
            sendOtpButton.isClickable = false
            val color = Color.parseColor("#A7A7A7")
            sendOtpButton.setBackgroundColor(color)
        }
    }
}