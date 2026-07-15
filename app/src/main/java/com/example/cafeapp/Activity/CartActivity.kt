package com.example.cafeapp.Activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafeapp.Adapter.CartAdapter
import com.example.cafeapp.Helper.ManagementCart
import com.example.cafeapp.databinding.ActivityCartBinding

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managementCart: ManagementCart
    private var tax: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagementCart(this)

        initCartList()
        calculateCart()
        setVariable()
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }

    private fun initCartList() {
        if (managementCart.getListCart().isEmpty()) {
            binding.emptyTxt.visibility = View.VISIBLE
            binding.cartView.visibility = View.GONE
            binding.constraintLayout.visibility = View.GONE
        } else {
            binding.emptyTxt.visibility = View.GONE
            binding.cartView.visibility = View.VISIBLE
            binding.constraintLayout.visibility = View.VISIBLE

            binding.cartView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.cartView.adapter = CartAdapter(managementCart.getListCart(), managementCart, object : ManagementCart.ChangeNumberItemsListener {
                override fun onChanged() {
                    calculateCart()
                }
            })
        }
    }

    private fun calculateCart() {
        val delivery = 15000.0
        val total = managementCart.getTotalFee()
        val allTotal = total + delivery

        binding.totalTxt.text = "${total.toInt()} VND"
        binding.deliveryTxt.text = "${delivery.toInt()} VND"
        binding.allTotalTxt.text = "${allTotal.toInt()} VND"
    }
}