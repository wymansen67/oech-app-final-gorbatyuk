package com.example.oche_app_final.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oche_app_final.R
import com.example.oche_app_final.activity.AddPaymentMethodActivity
import com.example.oche_app_final.databinding.FragmentWalletBinding
import com.example.oche_app_final.model.Constances
import com.example.oche_app_final.model.Transaction
import com.example.oche_app_final.model.WalletAdapter

class WalletFragment : Fragment() {
    private lateinit var wallet: FragmentWalletBinding
    private var isBalanceVisible = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        wallet = FragmentWalletBinding.inflate(inflater, container, false)
        return wallet.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (getSavedThemeState() == true) {
            setDarkTheme()
        } else {
            setLightTheme()
        }

        wallet.transactionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val transactions = listOf(
            Transaction("-N3,000.00", "Delivery fee", "July 7, 2022", false),
            Transaction("N2,000.00", "Lalalalala", "July 4, 2022", true),
            Transaction("-N3,000.00", "Third Delivery", "July 1, 2022", false),
            Transaction("N1,000.00", "Another One", "June 27, 2022", true),
            Transaction("N2,500.00", "Experts Are The Best", "June 23, 2022", true),
            Transaction("-N3,000.00", "Delivery fee", "June 17, 2022", false)
        )

        val adapter = WalletAdapter(transactions)
        wallet.transactionsRecyclerView.adapter = adapter

        wallet.walletImageView.setOnClickListener {
            startActivity(
                Intent(
                    this.requireContext(),
                    AddPaymentMethodActivity::class.java
                )
            )
        }

        wallet.ivVis.setOnClickListener {
            isBalanceVisible = !isBalanceVisible
            updateBalanceVisibility()
        }
        updateBalanceVisibility()
    }

    private fun updateBalanceVisibility() {
        if (isBalanceVisible) {
            wallet.tvProfileBalance.text = getString(R.string.examplebalance)
        } else {
            wallet.tvProfileBalance.text = "*****"
        }
    }

    private fun setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }
}