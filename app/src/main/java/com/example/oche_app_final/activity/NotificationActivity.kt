package com.example.oche_app_final.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.oche_app_final.R
import com.example.oche_app_final.databinding.ActivityNotificationBinding
import com.example.oche_app_final.model.Constances

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Notification"

        if (getSavedThemeState()) {
            setTheme(R.style.Theme_Ocheappfinal_Night)
            binding.layout.setBackgroundColor(resources.getColor(R.color.black_text_color, theme))
            binding.bell.imageTintList = resources.getColorStateList(R.color.white, theme)
            binding.textView.setTextColor(resources.getColor(R.color.white, theme))
        } else {
            setTheme(R.style.Theme_Ocheappfinal)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
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