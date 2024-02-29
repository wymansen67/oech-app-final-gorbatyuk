package com.example.oche_app_final.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.oche_app_final.R
import com.example.oche_app_final.databinding.ActivityNewPasswordBinding
import com.example.oche_app_final.model.Constances
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTheme(R.style.Theme_Ocheappfinal)

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val password: String = binding.passwordEditText.text.toString().trim()
                val passwordPattern =
                    Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
                val isValidPassword = password.matches(passwordPattern)
                if (!isValidPassword) {
                    binding.passwordEditText.error =
                        "The password must contain at least 8 characters, including upper and lower case letters, numbers and special characters."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                getChangePasswordButtonAccess(
                    binding.passwordEditText,
                    binding.confirmPasswordEditText,
                    binding.logInButton
                )
            }
        })

        binding.confirmPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val password: String = binding.passwordEditText.text.toString().trim()
                val confirmPassword: String = binding.confirmPasswordEditText.text.toString().trim()
                if (password != confirmPassword) {
                    binding.confirmPasswordEditText.error = "Password doesn't match."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                getChangePasswordButtonAccess(
                    binding.passwordEditText,
                    binding.confirmPasswordEditText,
                    binding.logInButton
                )
            }
        })

        binding.logInButton.setOnClickListener {
            val password = binding.passwordEditText.text.toString().trim()
            lifecycleScope.launch(Dispatchers.IO) {
                supabase.auth.modifyUser {
                    this.password = password
                }
                supabase.auth.sessionStatus.collect {
                    when (it) {
                        is SessionStatus.Authenticated -> {
                            val intent =
                                Intent(this@NewPasswordActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        else -> Toast.makeText(
                            this@NewPasswordActivity,
                            "Whoops! Something went wrong",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.passwordVisibilityToggle1.setOnClickListener {
            if (binding.passwordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                binding.passwordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.passwordEditText.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }
        binding.passwordVisibilityToggle2.setOnClickListener {
            if (binding.confirmPasswordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                binding.confirmPasswordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.confirmPasswordEditText.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }
    }

    val supabase = createSupabaseClient(
        supabaseUrl = "https://pnrpvncrwrvbmdxcewoi.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBucnB2bmNyd3J2Ym1keGNld29pIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDc5OTQwNTksImV4cCI6MjAyMzU3MDA1OX0.WaEuOMeUQZu-PVdCAVe4MuNRlsGqreHbyDNHKSbF3RM"
    ) {
        install(Auth)
    }

    private fun getChangePasswordButtonAccess(
        passwordEditText: TextView,
        confirmPasswordEditText: TextView,
        logInButton: Button
    ) {
        val passwordCheck =
            !passwordEditText.text.isNullOrEmpty() && passwordEditText.error.isNullOrEmpty()
        val confirmPasswordCheck =
            !confirmPasswordEditText.text.isNullOrEmpty() && confirmPasswordEditText.error.isNullOrEmpty()

        if (passwordCheck && confirmPasswordCheck) {
            logInButton.isClickable = true
            val color = Color.parseColor("#0560FA")
            logInButton.setBackgroundColor(color)
        } else {
            logInButton.isClickable = false
            val color = Color.parseColor("#A7A7A7")
            logInButton.setBackgroundColor(color)
        }
    }
}