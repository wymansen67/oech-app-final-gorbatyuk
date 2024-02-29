package com.example.oche_app_final.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.oche_app_final.R
import com.example.oche_app_final.databinding.ActivitySendPackageBinding
import com.example.oche_app_final.model.Constances

class SendPackageActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendPackageBinding
    private var correctWeight = true
    private var correctOrigPhone = true
    private var correctDetPhone = true
    private var correctDetPhone2 = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendPackageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Send a package"

        if (getSavedThemeState()) {
            setTheme(R.style.Theme_Ocheappfinal_Night)
            binding.layout.setBackgroundColor(resources.getColor(R.color.black_text_color, theme))
        } else {
            setTheme(R.style.Theme_Ocheappfinal)
            binding.layout.setBackgroundColor(resources.getColor(R.color.white, theme))
        }

        var add: ImageView = binding.AddDest
        add.setOnClickListener {
            var destTwo: LinearLayout = binding.llAddDest
            destTwo.visibility = VISIBLE
            add.visibility = GONE
        }

        binding.PackDetWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val weight: String = binding.PackDetWeight.text.toString().trim()
                val pattern = Regex("^[0-9]+")
                correctWeight = weight.matches(pattern)
                if (!correctWeight) {
                    binding.PackDetWeight.error =
                        "Incorrect input in the field for entering only numbers."
                } else if (weight.toInt() == 0) {
                    binding.PackDetWeight.error = "Less than 1."
                    correctWeight = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.OrigDetPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val phoneNumber: String = binding.OrigDetPhone.text.toString().trim()
                val phoneNumberPattern = Regex("^\\+7\\d{10}$")
                correctOrigPhone = phoneNumber.matches(phoneNumberPattern)
                if (!correctOrigPhone) {
                    binding.OrigDetPhone.error = "Please enter the correct phone number."
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.DesDet1Phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val phoneNumber: String = binding.DesDet1Phone.text.toString().trim()
                val phoneNumberPattern = Regex("^\\+7\\d{10}$")
                correctDetPhone = phoneNumber.matches(phoneNumberPattern)
                if (!correctDetPhone) {
                    binding.DesDet1Phone.error = "Please enter the correct phone number."
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.DesDet2Phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val phoneNumber: String = binding.DesDet2Phone.text.toString().trim()
                val phoneNumberPattern = Regex("^\\+7\\d{10}$")
                correctDetPhone2 = phoneNumber.matches(phoneNumberPattern)
                if (!correctDetPhone2) {
                    binding.DesDet2Phone.error = "Please enter the correct phone number."
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    fun instantDelivery(view: View) {

        var two = false
        var origAddress: String = binding.OrigDetAddress.text.toString()
        var origCountry: String = binding.OrigDetCountry.text.toString()
        var origPhone: String = binding.OrigDetPhone.text.toString()

        if (origAddress.isNullOrEmpty()) {
            binding.OrigDetAddress.error = "Empty"
        } else if (origCountry.isNullOrEmpty()) {
            binding.OrigDetCountry.error = "Empty"
        } else if (origPhone.isNullOrEmpty()) {
            binding.OrigDetPhone.error = "Empty"
        } else {

            var destAddress1: String = binding.DesDet1Address.text.toString()
            var destCountry1: String = binding.DesDet1Country.text.toString()
            var destPhone1: String = binding.DesDet1Phone.text.toString()

            if (destAddress1.isNullOrEmpty()) {
                binding.DesDet1Address.error = "Empty"
            } else if (destCountry1.isNullOrEmpty()) {
                binding.DesDet1Country.error = "Empty"
            } else if (destPhone1.isNullOrEmpty()) {
                binding.DesDet1Phone.error = "Empty"
            } else {
                if (!binding.DesDet2Address.text.toString().isNullOrEmpty()) {
                    if (binding.DesDet2Country.text.toString().isNullOrEmpty()) {
                        binding.DesDet2Country.error = "Empty"
                    } else if (binding.DesDet2Phone.text.toString().isNullOrEmpty()) {
                        binding.DesDet2Phone.error = "Empty"
                    } else {
                        two = true
                    }
                } else {
                    binding.DesDet2Address.setText("")
                    binding.DesDet2Country.setText("")
                    binding.DesDet2Phone.setText("")
                }

                var packItems: String = binding.PackDetItmes.text.toString()
                var packWeight: String = binding.PackDetWeight.text.toString()
                var packWorth: String = binding.PackDetWorth.text.toString()

                if (packItems.isNullOrEmpty()) {
                    binding.PackDetItmes.error = "Empty"
                } else if (packWeight.isNullOrEmpty()) {
                    binding.PackDetWeight.error = "Empty"
                } else if (packWorth.isNullOrEmpty()) {
                    binding.PackDetWorth.error = "Empty"
                } else if (correctWeight && correctOrigPhone && correctDetPhone) {

                    val intent = Intent(this, RecieptActivity::class.java)
                    intent.putExtra(Constances.CODE, Constances.ONE)

                    val shPref: SharedPreferences =
                        getSharedPreferences(Constances.PACKAGE, MODE_PRIVATE)

                    val edit = shPref.edit()

                    edit.putString(Constances.OR_DET_ADDRESS, origAddress)
                    edit.putString(Constances.OR_DET_PHONE, origPhone)

                    var destDet: String = "1. $destAddress1\n$destPhone1"
                    if (two) {
                        destDet += "\n2. ${binding.DesDet2Address.text}\n${binding.DesDet2Phone.text}"
                    }
                    edit.putString(Constances.DESTINATION_DETAILS, destDet)

                    edit.putString(Constances.PACKAGE_ITEMS, packItems)
                    edit.putString(Constances.WEIGHT, packWeight)
                    edit.putString(Constances.WORTH, packWorth)
                    edit.apply()

                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }
}