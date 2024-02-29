package com.example.oche_app_final.model

import java.io.Serializable

data class ChatDriver(
    val profileImage: Int,
    val name: String,
    val registrationNumber: String,
    val stars: Int,
    val carModel: String,
    val gender: String
) : Serializable
