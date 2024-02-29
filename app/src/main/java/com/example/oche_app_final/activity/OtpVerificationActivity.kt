package com.example.oche_app_final.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.oche_app_final.R
import com.example.oche_app_final.databinding.ActivityOtpVrificationBinding
import com.google.android.material.snackbar.Snackbar
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OtpVerificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityOtpVrificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVrificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTheme(R.style.Theme_Ocheappfinal)

        val rootView: View = findViewById<View>(android.R.id.content)

        val bluePrimaryColor = ContextCompat.getColor(this, R.color.blue_primary)
        val grayColor = ContextCompat.getColor(this, R.color.gray_color)

        val initialTime = 1 * 60 * 1000
        var currentTime = initialTime
        val timer = object : CountDownTimer(currentTime.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resendTextView.setTextColor(grayColor)
                currentTime = millisUntilFinished.toInt()
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.resendTextView.text =
                    String.format("resend after %d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.resendTextView.text = "resend"
                binding.resendTextView.setTextColor(bluePrimaryColor)

                val widthInPixels = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    416.toFloat(),
                    resources.displayMetrics
                ).toInt()

                val params = binding.textView3.layoutParams
                params.width = widthInPixels
                binding.textView3.layoutParams = params

                binding.resendTextView.isClickable = true
            }
        }
        timer.start()

        binding.resendTextView.setOnClickListener {
            val email = intent.getStringExtra("email")
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val user = supabase.auth.resetPasswordForEmail(email!!)
                } catch (e: Exception) {
                    Log.d("otpException", e.toString())
                    Snackbar.make(rootView, "Wrong code", Snackbar.LENGTH_SHORT).show()
                }
            }

            val widthInPixels = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                354.toFloat(),
                resources.displayMetrics
            ).toInt()
            val params = binding.textView3.layoutParams
            params.width = widthInPixels
            binding.textView3.layoutParams = params
            binding.resendTextView.isClickable = false
            timer.start()
        }

        binding.sendOtpButton.setOnClickListener {
            val token = binding.editText1.text.toString().trim() + binding.editText2.text.toString()
                .trim() + binding.editText3.text.toString()
                .trim() + binding.editText4.text.toString()
                .trim() + binding.editText5.text.toString()
                .trim() + binding.editText6.text.toString().trim()
            Log.d("token", token)
            val email = intent.getStringExtra("email")
            Log.d("email", email.toString())
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val user = supabase.auth.verifyEmailOtp(OtpType.Email.RECOVERY, email!!, token)
                    Log.d("user", user.toString())
                    supabase.auth.sessionStatus.collect {
                        when (it) {
                            is SessionStatus.Authenticated -> {
                                val intent =
                                    Intent(
                                        this@OtpVerificationActivity,
                                        NewPasswordActivity::class.java
                                    )
                                startActivity(intent)
                                finish()
                            }

                            SessionStatus.NetworkError -> {}

                            SessionStatus.NotAuthenticated -> {}

                            else -> {}
                        }
                    }
                } catch (e: Exception) {
                    Log.d("otpException", e.toString())
                    binding.editText1.text.clear()
                    binding.editText2.text.clear()
                    binding.editText3.text.clear()
                    binding.editText4.text.clear()
                    binding.editText5.text.clear()
                    binding.editText6.text.clear()
                    Snackbar.make(rootView, "Wrong code", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.editText1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.editText1.setBackgroundResource(R.drawable.otp_verification_edittext)
                    return
                }
                if (s.length == 1) {
                    binding.editText1.setBackgroundResource(R.drawable.otp_verification_edittext_filled)
                    binding.editText2.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.editText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.editText2.clearFocus()
                    binding.editText1.requestFocus()
                    binding.editText2.setBackgroundResource(R.drawable.otp_verification_edittext)
                    return
                }
                if (s.length == 1) {
                    binding.editText3.requestFocus()
                    binding.editText2.setBackgroundResource(R.drawable.otp_verification_edittext_filled)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editText3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.editText3.clearFocus()
                    binding.editText2.requestFocus()
                    binding.editText3.setBackgroundResource(R.drawable.otp_verification_edittext)
                    return
                }
                if (s.length == 1) {
                    binding.editText4.requestFocus()
                    binding.editText3.setBackgroundResource(R.drawable.otp_verification_edittext_filled)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editText4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.editText4.clearFocus()
                    binding.editText3.requestFocus()
                    binding.editText4.setBackgroundResource(R.drawable.otp_verification_edittext)
                    return
                }
                if (s.length == 1) {
                    binding.editText5.requestFocus()
                    binding.editText4.setBackgroundResource(R.drawable.otp_verification_edittext_filled)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editText5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.editText5.clearFocus()
                    binding.editText4.requestFocus()
                    binding.editText5.setBackgroundResource(R.drawable.otp_verification_edittext)
                    return
                }
                if (s.length == 1) {
                    binding.editText6.requestFocus()
                    binding.editText5.setBackgroundResource(R.drawable.otp_verification_edittext_filled)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editText6.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.editText6.clearFocus()
                    binding.editText5.requestFocus()
                    binding.editText6.setBackgroundResource(R.drawable.otp_verification_edittext)
                    return
                }
                if (s.length == 1) {
                    binding.editText6.setBackgroundResource(R.drawable.otp_verification_edittext_filled)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    val supabase = createSupabaseClient(
        supabaseUrl = "https://pnrpvncrwrvbmdxcewoi.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBucnB2bmNyd3J2Ym1keGNld29pIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDc5OTQwNTksImV4cCI6MjAyMzU3MDA1OX0.WaEuOMeUQZu-PVdCAVe4MuNRlsGqreHbyDNHKSbF3RM"
    ) {
        install(Auth)
    }
}