package com.example.oche_app_final.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.oche_app_final.R
import com.example.oche_app_final.model.Constances

class AddPaymentMethodActivity : AppCompatActivity() {

    private lateinit var radioButton1: RadioButton
    private lateinit var radioButton2: RadioButton
    private lateinit var radioButton2_1: RadioButton
    private lateinit var radioButton2_2: RadioButton
    private lateinit var linearLayout1: LinearLayout
    private lateinit var linearLayout2: LinearLayout
    private lateinit var linearLayout2_2: LinearLayout
    private lateinit var checkbox2_1LinearLayout: LinearLayout
    private lateinit var checkbox2_2LinearLayout: LinearLayout
    private lateinit var layoutParams: ConstraintLayout.LayoutParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payment_method)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Payment method"

        if (getSavedThemeState()) {
            setTheme(R.style.Theme_Ocheappfinal_Night)
        } else {
            setTheme(R.style.Theme_Ocheappfinal)
        }

        radioButton1 = findViewById(R.id.radioButton1)
        radioButton2 = findViewById(R.id.radioButton2)
        radioButton2_1 = findViewById(R.id.radioButton2_1)
        radioButton2_2 = findViewById(R.id.radioButton2_2)
        linearLayout1 = findViewById(R.id.linearLayout1)
        linearLayout2 = findViewById(R.id.linearLayout2)
        linearLayout2_2 = findViewById(R.id.linearLayout2_2)
        checkbox2_1LinearLayout = findViewById(R.id.checkbox2_1LinearLayout)
        checkbox2_2LinearLayout = findViewById(R.id.checkbox2_2LinearLayout)

        radioButton1.isChecked = true
        radioButton2_1.isChecked = true
        linearLayout2_2.visibility = LinearLayout.GONE

        layoutParams = linearLayout2.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_84dp)
        linearLayout2.layoutParams = layoutParams

        linearLayout1.setOnClickListener {
            radioButton1.isChecked = true
            radioButton1True(it)
        }

        linearLayout2.setOnClickListener {
            if (radioButton2.isChecked) return@setOnClickListener
            radioButton2.isChecked = true
            radioButton2True(it)
        }

        radioButton1.setOnClickListener {
            radioButton1True(it)
        }

        radioButton2.setOnClickListener {
            if (!radioButton2.isChecked || !radioButton1.isChecked) return@setOnClickListener
            radioButton2True(it)
        }

        checkbox2_1LinearLayout.setOnClickListener {
            radioButton2_1.isChecked = true
            radioButton2_1True(it)
        }

        checkbox2_2LinearLayout.setOnClickListener {
            radioButton2_2.isChecked = true
            radioButton2_2True(it)
        }

        radioButton2_1.setOnClickListener {
            radioButton2_1True(it)
        }

        radioButton2_2.setOnClickListener {
            radioButton2_2True(it)
        }
    }

    private fun radioButton1True(view: View) {
        linearLayout2_2.visibility = LinearLayout.GONE
        radioButton2.isChecked = false
        layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_84dp)
        linearLayout2.layoutParams = layoutParams
    }

    private fun radioButton2True(view: View) {
        linearLayout2_2.visibility = LinearLayout.VISIBLE
        radioButton1.isChecked = false
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        linearLayout2.layoutParams = layoutParams
    }

    private fun radioButton2_1True(view: View) {
        radioButton2_1.isChecked = true
        radioButton2_2.isChecked = false
    }

    private fun radioButton2_2True(view: View) {
        radioButton2_1.isChecked = false
        radioButton2_2.isChecked = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }
}