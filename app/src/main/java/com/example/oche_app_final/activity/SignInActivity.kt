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
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.oche_app_final.R
import com.example.oche_app_final.databinding.ActivitySignInBinding
import com.example.oche_app_final.model.Constances
import com.google.android.material.snackbar.Snackbar
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTheme(R.style.Theme_Ocheappfinal)

        val rootView = findViewById<View>(android.R.id.content)

        binding.passwordVisibilityToggle.setOnClickListener {
            if (binding.passwordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                binding.passwordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.passwordEditText.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
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
                getSignUpButtonAccess(
                    binding.emailAddressEditText,
                    binding.passwordEditText,
                    binding.logInButton
                )
            }
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val password: String = binding.passwordEditText.text.toString().trim()
                if (password.isNullOrEmpty()) {
                    binding.passwordEditText.error =
                        "Enter password"
                }
            }

            override fun afterTextChanged(s: Editable?) {
                getSignUpButtonAccess(
                    binding.emailAddressEditText,
                    binding.passwordEditText,
                    binding.logInButton
                )
            }
        })

        binding.forgotPasswordTextView.setOnClickListener {
            val intent =
                Intent(this@SignInActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.logInButton.setOnClickListener {
            val email = binding.emailAddressEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val user = supabase.auth.signInWith(Email) {
                        this.email = email
                        this.password = password
                    }
                    supabase.auth.sessionStatus.collect {
                        when (it) {
                            is SessionStatus.Authenticated -> {
                                if (binding.rememberPassword.isChecked) {
                                    val session = supabase.auth.currentSessionOrNull()
                                    if (session != null) {
                                        supabase.auth.sessionManager.saveSession(session)
                                    }
                                }
                                val intent =
                                    Intent(this@SignInActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                            SessionStatus.NetworkError -> Toast.makeText(
                                this@SignInActivity,
                                "Network error",
                                Toast.LENGTH_LONG
                            ).show()

                            SessionStatus.NotAuthenticated -> Toast.makeText(
                                this@SignInActivity,
                                "Wrong email or password",
                                Toast.LENGTH_LONG
                            ).show()

                            else -> Toast.makeText(
                                this@SignInActivity,
                                "Whoops! Something went wrong",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (error: Exception) {
                    Log.d("logInException", error.toString())
                    var snackbar =
                        Snackbar.make(it, R.string.something_wrong, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        }

        binding.signInGoogleImage.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    signInGoogle()
                    supabase.auth.sessionStatus.collect {
                        when (it) {
                            is SessionStatus.Authenticated -> {
                                val intent = Intent(this@SignInActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                            SessionStatus.NetworkError -> getError(rootView, "Network Error")

                            SessionStatus.NotAuthenticated -> getError(
                                rootView,
                                "Authorization failed"
                            )

                            SessionStatus.LoadingFromStorage -> getError(
                                rootView,
                                "Loading session"
                            )

                            else -> getError(rootView, "Whoops! Something went wrong")
                        }
                    }
                } catch (error: Exception) {
                    Log.d("logInException", error.toString())
                    var snackbar =
                        Snackbar.make(it, R.string.something_wrong, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        }

        binding.signUnTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    val supabase = createSupabaseClient(
        supabaseUrl = "https://pnrpvncrwrvbmdxcewoi.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBucnB2bmNyd3J2Ym1keGNld29pIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDc5OTQwNTksImV4cCI6MjAyMzU3MDA1OX0.WaEuOMeUQZu-PVdCAVe4MuNRlsGqreHbyDNHKSbF3RM"
    ) {
        install(Auth)
    }

    suspend fun signInGoogle() {
        withContext(Dispatchers.IO) {
            val user = supabase.auth.signInWith(Google)
        }
    }

    private fun getSignUpButtonAccess(
        emailAddressEditText: TextView,
        passwordEditText: TextView,
        signInButton: Button,
    ) {
        val emailCheck =
            !emailAddressEditText.text.isNullOrEmpty() && emailAddressEditText.error.isNullOrEmpty()
        val passwordCheck =
            !passwordEditText.text.isNullOrEmpty() && passwordEditText.error.isNullOrEmpty()

        if (emailCheck && passwordCheck) {
            signInButton.isClickable = true
            val color = Color.parseColor("#0560FA")
            signInButton.setBackgroundColor(color)
        } else {
            signInButton.isClickable = false
            val color = Color.parseColor("#A7A7A7")
            signInButton.setBackgroundColor(color)
        }
    }

    private fun getError(view: View, error: String) {
        Snackbar.make(view, error, Snackbar.LENGTH_LONG).show()
    }
}
