package com.example.oche_app_final.model

data class Transaction(
    val amount: String,
    val description: String,
    val date: String,
    val isIncome: Boolean
)