package com.example.oche_app_final.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.oche_app_final.R
import com.example.oche_app_final.databinding.ActivitySignUpBinding
import com.example.oche_app_final.model.Constances
import com.google.android.material.snackbar.Snackbar
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTheme(R.style.Theme_Ocheappfinal)

        val rootView = findViewById<View>(android.R.id.content)

        val text = getString(R.string.private_policy)
        val spannableString = SpannableString(text)
        val startIndex = text.indexOf("our")
        if (startIndex != -1) {

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    val file = File(cacheDir, "privacy_policy_oche_app_final.pdf")
                    val fileOutputStream = FileOutputStream(file)
                    val inputStream = assets.open("privacy_policy_oche_app_final.pdf")

                    try {
                        inputStream.use { input ->
                            fileOutputStream.use { output ->
                                input.copyTo(output)
                            }
                        }
                    } finally {
                        fileOutputStream.close()
                        inputStream.close()
                    }

                    val fileUri = FileProvider.getUriForFile(
                        this@SignUpActivity,
                        applicationContext.packageName + ".provider",
                        file
                    )
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(fileUri, "application/pdf")
                    intent.flags =
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
                    startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    val highlightColor =
                        ContextCompat.getColor(this@SignUpActivity, R.color.yellow_warning_color)
                    ds.color = highlightColor
                    ds.isUnderlineText = false
                }
            }

            spannableString.setSpan(
                clickableSpan,
                startIndex,
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        binding.textView8.movementMethod = LinkMovementMethod.getInstance()
        binding.textView8.text = spannableString

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

        binding.fullNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val fullName: String = binding.fullNameEditText.text.toString().trim()
                val fullNamePattern = Regex("[А-Яа-яA-Za-z]+\\s[А-Яа-яA-Za-z]+")
                val isValidFullName = fullName.matches(fullNamePattern)
                if (!isValidFullName) {
                    binding.fullNameEditText.error =
                        "Please enter your full name in the format 'Last Name First Name'."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                getSignUpButtonAccess(
                    binding.fullNameEditText,
                    binding.phoneNumberEditText,
                    binding.emailAddressEditText,
                    binding.passwordEditText,
                    binding.confirmPasswordEditText,
                    binding.signUpButton,
                    binding.agreementCheckBox
                )
            }
        })

        binding.phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val phoneNumber: String = binding.phoneNumberEditText.text.toString().trim()
                val phoneNumberPattern = Regex("^\\+7\\d{10}$")
                val isValidPhoneNumber = phoneNumber.matches(phoneNumberPattern)
                if (!isValidPhoneNumber) {
                    binding.phoneNumberEditText.error = "Please enter the correct phone number."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                getSignUpButtonAccess(
                    binding.fullNameEditText,
                    binding.phoneNumberEditText,
                    binding.emailAddressEditText,
                    binding.passwordEditText,
                    binding.confirmPasswordEditText,
                    binding.signUpButton,
                    binding.agreementCheckBox
                )
            }
        })

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
                    binding.fullNameEditText,
                    binding.phoneNumberEditText,
                    binding.emailAddressEditText,
                    binding.passwordEditText,
                    binding.confirmPasswordEditText,
                    binding.signUpButton,
                    binding.agreementCheckBox
                )
            }
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

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
                getSignUpButtonAccess(
                    binding.fullNameEditText,
                    binding.phoneNumberEditText,
                    binding.emailAddressEditText,
                    binding.passwordEditText,
                    binding.confirmPasswordEditText,
                    binding.signUpButton,
                    binding.agreementCheckBox
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
                getSignUpButtonAccess(
                    binding.fullNameEditText,
                    binding.phoneNumberEditText,
                    binding.emailAddressEditText,
                    binding.passwordEditText,
                    binding.confirmPasswordEditText,
                    binding.signUpButton,
                    binding.agreementCheckBox
                )
            }
        })

        binding.agreementCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            getSignUpButtonAccess(
                binding.fullNameEditText,
                binding.phoneNumberEditText,
                binding.emailAddressEditText,
                binding.passwordEditText,
                binding.confirmPasswordEditText,
                binding.signUpButton,
                binding.agreementCheckBox
            )
        }

        binding.signInTextView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.signUpButton.setOnClickListener {
            val email = binding.emailAddressEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val user = supabase.auth.signUpWith(Email) {
                        this.email = email
                        this.password = password
                    }
                    if (user.toString().isNotEmpty()) {
                        getError(rootView, "Registration successfully ")
                        val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } catch (error: Exception) {
                    Log.d("logInException", error.toString())
                    val snackbar =
                        Snackbar.make(it, R.string.something_wrong, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        }

        binding.signInGoogleImage.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val user = supabase.auth.signUpWith(Google)
                    if (user.toString().isNotEmpty()) {
                        getError(rootView, "Registration successfully ")
                        val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } catch (error: Exception) {
                    Log.d("logInException", error.toString())
                    var snackbar =
                        Snackbar.make(it, R.string.something_wrong, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        }
    }

    private fun getSignUpButtonAccess(
        fullNameEditText: TextView,
        phoneNumberEditText: TextView,
        emailAddressEditText: TextView,
        passwordEditText: TextView,
        confirmPasswordEditText: TextView,
        signUpButton: Button,
        agrmntCheckBox: CheckBox
    ) {
        val fullNameCheck =
            !fullNameEditText.text.isNullOrEmpty() && fullNameEditText.error.isNullOrEmpty()
        val phoneNumberCheck =
            !phoneNumberEditText.text.isNullOrEmpty() && phoneNumberEditText.error.isNullOrEmpty()
        val emailCheck =
            !emailAddressEditText.text.isNullOrEmpty() && emailAddressEditText.error.isNullOrEmpty()
        val passwordCheck =
            !passwordEditText.text.isNullOrEmpty() && passwordEditText.error.isNullOrEmpty()
        val confirmPasswordCheck =
            !confirmPasswordEditText.text.isNullOrEmpty() && confirmPasswordEditText.error.isNullOrEmpty()

        if (fullNameCheck && phoneNumberCheck && emailCheck && passwordCheck && confirmPasswordCheck && agrmntCheckBox.isChecked) {
            signUpButton.isClickable = true
            val color = Color.parseColor("#0560FA")
            signUpButton.setBackgroundColor(color)
        } else {
            signUpButton.isClickable = false
            val color = Color.parseColor("#A7A7A7")
            signUpButton.setBackgroundColor(color)
        }
    }

    //Creates supabase client using url and key stored in a separate file
    val supabase = createSupabaseClient(
        supabaseUrl = "https://pnrpvncrwrvbmdxcewoi.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBucnB2bmNyd3J2Ym1keGNld29pIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDc5OTQwNTksImV4cCI6MjAyMzU3MDA1OX0.WaEuOMeUQZu-PVdCAVe4MuNRlsGqreHbyDNHKSbF3RM"
    ) {
        install(Auth)
    }

    fun registredUser(view: View, error: String) {
        val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
        snackbar.setAction("Want to login?") {
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        snackbar.setActionTextColor(ContextCompat.getColor(view.context, R.color.blue_primary))
        snackbar.show()
    }

    private fun getError(view: View, error: String) {
        Snackbar.make(view, error, Snackbar.LENGTH_LONG).show()
    }
}