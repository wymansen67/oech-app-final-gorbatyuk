package com.example.oche_app_final.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val fullname: String,
    val phonenumber: String,
    val email: String,
    val password: String
)