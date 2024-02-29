package com.example.oche_app_final.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.example.oche_app_final.R
import com.example.oche_app_final.databinding.ActivityRecieptBinding
import com.example.oche_app_final.model.Constances

class RecieptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecieptBinding
    private var signState = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecieptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Send a package"

        if (getSavedThemeState()) {
            setTheme(R.style.Theme_Ocheappfinal_Night)
            binding.layout.setBackgroundColor(resources.getColor(R.color.black_text_color, theme))
            binding.buttonEditPackage.setBackgroundColor(
                resources.getColor(
                    R.color.black_text_color,
                    theme
                )
            )
            binding.buttonReport.setBackgroundColor(
                resources.getColor(
                    R.color.black_text_color,
                    theme
                )
            )
        } else {
            setTheme(R.style.Theme_Ocheappfinal)
            binding.layout.setBackgroundColor(resources.getColor(R.color.white, theme))
        }

        val shPref: SharedPreferences = getSharedPreferences(Constances.PACKAGE, MODE_PRIVATE)
        binding.orAddress.text = shPref.getString(Constances.OR_DET_ADDRESS, "").toString()
        binding.orPhone.text = shPref.getString(Constances.OR_DET_PHONE, "").toString()
        binding.DestinationDetails.text =
            shPref.getString(Constances.DESTINATION_DETAILS, "").toString()
        binding.otherPackageItems.text = shPref.getString(Constances.PACKAGE_ITEMS, "").toString()
        binding.otherWeight.text = shPref.getString(Constances.WEIGHT, "").toString()
        binding.otherWorth.text = shPref.getString(Constances.WORTH, "").toString()

        signState = intent.getStringExtra(Constances.CODE)!!

        when (signState) {
            Constances.ONE -> {
                binding.ControlPackage.visibility = VISIBLE
            }

            Constances.TWO -> {
                binding.ControlPackage.visibility = GONE
                binding.DeliverySuccessful.visibility = VISIBLE
            }

            else -> {
                binding.DeliverySuccessful.visibility = VISIBLE
            }
        }

        binding.buttonEditPackage.setOnClickListener {
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.buttonMakePayment.setOnClickListener {
            val intent = Intent(this@RecieptActivity, TransactionSuccessfulActivity::class.java)
            intent.putExtra(Constances.CODE, Constances.ONE)
            startActivity(intent)
            finish()
        }
        binding.buttonEditPackage.setOnClickListener {
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.buttonSuccessful.setOnClickListener {
            val intent = Intent(this@RecieptActivity, TransactionSuccessfulActivity::class.java)
            intent.putExtra(Constances.CODE, Constances.TWO)
            startActivity(intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            setResult(RESULT_OK, intent)
            finish()
        }
        return true
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }
}