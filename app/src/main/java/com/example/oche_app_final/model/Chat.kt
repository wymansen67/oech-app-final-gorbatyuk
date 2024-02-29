package com.example.oche_app_final.model

data class Chat(
    val profileImage: Int,
    val name: String,
    val lastMessage: String,
    val unreadCount: Int
)