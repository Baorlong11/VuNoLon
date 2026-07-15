package com.example.cafeapp.Activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafeapp.Adapter.CartAdapter
import com.example.cafeapp.Repository.CartManager
import com.example.cafeapp.databinding.ActivityCartBinding

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }

        loadCart()

        binding.btnCheckout.setOnClickListener {
            val total = CartManager.getTotalPrice(this)
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("totalPrice", total)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload lại giỏ hàng mỗi lần quay lại màn này (vd sau khi thêm món mới)
        loadCart()
    }

    private fun loadCart() {
        val cartItems = CartManager.getCartItems(this)

        binding.recyclerCart.layoutManager = LinearLayoutManager(this)
        binding.recyclerCart.adapter = CartAdapter(cartItems) {
            updateTotal()
        }

        binding.txtEmptyCart.visibility =
            if (cartItems.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE

        updateTotal()
    }

    private fun updateTotal() {
        val total = CartManager.getTotalPrice(this)
        binding.txtTotalCart.text = "Tổng tiền: ${total.toInt()}đ"
    }
}