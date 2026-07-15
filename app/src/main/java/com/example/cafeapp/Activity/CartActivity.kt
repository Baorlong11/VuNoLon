package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.adapter.CartAdapter
import com.example.cafeapp.helper.ManagementCart

class CartActivity : BaseActivity() {
    private lateinit var managementCart: ManagementCart
    
    private lateinit var totalTxt: TextView
    private lateinit var deliveryTxt: TextView
    private lateinit var allTotalTxt: TextView
    private lateinit var emptyTxt: TextView
    private lateinit var cartView: RecyclerView
    private lateinit var constraintLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        managementCart = ManagementCart(this)

        initViews()
        initCartList()
        calculateCart()
        setVariable()
    }

    private fun initViews() {
        totalTxt = findViewById(R.id.totalTxt)
        deliveryTxt = findViewById(R.id.deliveryTxt)
        allTotalTxt = findViewById(R.id.allTotalTxt)
        emptyTxt = findViewById(R.id.emptyTxt)
        cartView = findViewById(R.id.cartView)
        constraintLayout = findViewById(R.id.constraintLayout)
    }

    private fun setVariable() {
        findViewById<ImageView>(R.id.backBtn).setOnClickListener { finish() }
        findViewById<View>(R.id.checkoutBtn).setOnClickListener {
            if (managementCart.getListCart().isEmpty()) {
                android.widget.Toast.makeText(this, "Giỏ hàng trống", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val delivery = 15000.0
            val total = managementCart.getTotalFee()
            val allTotal = total + delivery
            
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("total", allTotal)
            startActivity(intent)
        }
    }

    private fun initCartList() {
        val listCart = managementCart.getListCart()
        if (listCart.isEmpty()) {
            emptyTxt.visibility = View.VISIBLE
            cartView.visibility = View.GONE
            constraintLayout.visibility = View.GONE
        } else {
            emptyTxt.visibility = View.GONE
            cartView.visibility = View.VISIBLE
            constraintLayout.visibility = View.VISIBLE

            cartView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            cartView.adapter = CartAdapter(listCart, managementCart, object : ManagementCart.ChangeNumberItemsListener {
                override fun onChanged() {
                    calculateCart()
                    if (managementCart.getListCart().isEmpty()) {
                        emptyTxt.visibility = View.VISIBLE
                        cartView.visibility = View.GONE
                        constraintLayout.visibility = View.GONE
                    }
                }
            })
        }
    }

    private fun calculateCart() {
        val delivery = 15000.0
        val total = managementCart.getTotalFee()
        val allTotal = total + delivery

        totalTxt.text = "${total.toInt()} VND"
        deliveryTxt.text = "${delivery.toInt()} VND"
        allTotalTxt.text = "${allTotal.toInt()} VND"
    }
}
