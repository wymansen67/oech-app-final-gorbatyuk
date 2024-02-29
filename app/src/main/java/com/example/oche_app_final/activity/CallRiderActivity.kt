package com.example.oche_app_final.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.oche_app_final.R
import com.example.oche_app_final.databinding.ActivityCallRiderBinding
import com.example.oche_app_final.model.Constances

class CallRiderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCallRiderBinding

    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallRiderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (getSavedThemeState()) {
            setTheme(R.style.Theme_Ocheappfinal_Night)
            binding.layout.setBackgroundColor(resources.getColor(R.color.black_text_color, theme))
            binding.functions.setBackgroundColor(resources.getColor(R.color.black, theme))
            binding.button1.imageTintList = resources.getColorStateList(R.color.white, theme)
            binding.button2.imageTintList = resources.getColorStateList(R.color.white, theme)
            binding.button3.imageTintList = resources.getColorStateList(R.color.white, theme)
            binding.button4.imageTintList = resources.getColorStateList(R.color.white, theme)
            binding.button5.imageTintList = resources.getColorStateList(R.color.white, theme)
            binding.button6.imageTintList = resources.getColorStateList(R.color.white, theme)
        } else {
            setTheme(R.style.Theme_Ocheappfinal)
            binding.layout.setBackgroundColor(resources.getColor(R.color.white, theme))
        }

        val chatName = intent.getStringExtra("chatName")
        val chatImageName = intent.getStringExtra("chatImage")

        val chatImageView: ImageView = binding.chatImageView
        val callerTextView: TextView = binding.callerTextView
        val endCallImageView: ImageView = binding.endCallImageView

        callerTextView.text = chatName

        if (chatImageName != null) {
            val resourceId = resources.getIdentifier(chatImageName, "drawable", packageName)
            if (resourceId != 0) {
                chatImageView.setImageResource(resourceId)
            } else {
                chatImageView.setImageResource(R.drawable.ic_profile)
            }
        } else {
            chatImageView.setImageResource(R.drawable.ic_profile)
        }

        endCallImageView.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
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