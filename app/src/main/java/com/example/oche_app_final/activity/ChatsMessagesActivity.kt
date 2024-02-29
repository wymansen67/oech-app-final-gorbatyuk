package com.example.oche_app_final.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oche_app_final.R
import com.example.oche_app_final.databinding.ActivityChatsMessagesBinding
import com.example.oche_app_final.model.Constances
import com.example.oche_app_final.model.Message
import com.example.oche_app_final.model.MessageAdapter

class ChatsMessagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatsMessagesBinding

    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatsMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatName = intent.getStringExtra("chatName")
        val chatImageName = intent.getStringExtra("chatImage")

        val backIcon: ImageView = binding.backIcon
        val callIcon: ImageView = binding.callIcon
        val actionbarText: TextView = binding.actionbarText
        val chatImageView: ImageView = binding.chatImageView

        actionbarText.text = chatName

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

        backIcon.setOnClickListener {
            onBackPressed()
        }

        val messagesRecyclerView: RecyclerView = findViewById(R.id.messagesRecyclerView)

        val layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.layoutManager = layoutManager

        val messages = mutableListOf(
            Message(
                "Hello, Please check your phone, I just booked you to deliver my stuff",
                isFromMe = true
            ),
            Message("Thank you for contacting me.", isFromMe = false),
            Message("I am already on my way to the pick up venue.", isFromMe = false),
            Message("Alright, I wll be waiting", isFromMe = true)
        )

        val adapter = MessageAdapter(messages)

        if (getSavedThemeState()) {
            binding.layout.setBackgroundColor(resources.getColor(R.color.black, theme))
            binding.actionbarLayout.setBackgroundColor(
                resources.getColor(
                    R.color.black_text_color,
                    theme
                )
            )
            adapter.dark = true
        } else {
            binding.layout.setBackgroundColor(resources.getColor(R.color.white, theme))
            adapter.dark = false
        }

        messagesRecyclerView.adapter = adapter

        val enterImageView: ImageView = findViewById(R.id.enterImageView)
        val searchChatsEditText: EditText = findViewById(R.id.searchChatsEditText)
        val onlineTextView: TextView = findViewById(R.id.onlineTextView)

        if (Constances.isCustomer) {
            onlineTextView.visibility = View.VISIBLE
        } else {
            onlineTextView.visibility = View.GONE
        }

        enterImageView.setOnClickListener {
            val messageText = searchChatsEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val newMessage = Message(messageText, isFromMe = true)
                messages += newMessage
                adapter.notifyItemInserted(messages.size - 1)
                searchChatsEditText.text.clear()

                messagesRecyclerView.scrollToPosition(messages.size - 1)
            }
        }

        callIcon.setOnClickListener {
            val intent = Intent(this, CallRiderActivity::class.java)
            intent.putExtra("chatName", chatName)
            intent.putExtra("chatImage", chatImageName.toString())
            startActivity(intent)
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