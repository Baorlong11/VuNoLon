package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.cafeapp.R
import com.example.cafeapp.helper.ManagementCart
import com.example.cafeapp.model.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

class CheckoutActivity : BaseActivity() {
    private lateinit var managementCart: ManagementCart
    private var totalAmount: Double = 0.0
    private lateinit var progressBar: ProgressBar
    private lateinit var placeOrderBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        managementCart = ManagementCart(this)
        totalAmount = intent.getDoubleExtra("total", 0.0)

        initViews()
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progressBarCheckout)
        placeOrderBtn = findViewById(R.id.placeOrderBtn)
        
        val totalTxt = findViewById<TextView>(R.id.totalCheckoutTxt)
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        val edtName = findViewById<EditText>(R.id.edtName)
        val edtPhone = findViewById<EditText>(R.id.edtPhone)
        val edtAddress = findViewById<EditText>(R.id.edtAddress)

        totalTxt.text = "${totalAmount.toInt()} VND"
        backBtn.setOnClickListener { finish() }

        placeOrderBtn.setOnClickListener {
            val name = edtName.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val address = edtAddress.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            placeOrder(name, phone, address)
        }
    }

    private fun placeOrder(name: String, phone: String, address: String) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val cartItems = managementCart.getListCart()

        progressBar.visibility = View.VISIBLE
        placeOrderBtn.isEnabled = false
        Toast.makeText(this, "Đang gửi đơn hàng...", Toast.LENGTH_SHORT).show()
        
        val database = FirebaseDatabase.getInstance()
        val orderRef = database.getReference("Orders").push()
        val orderId = orderRef.key ?: ""

        val order = OrderModel(
            orderId = orderId,
            userEmail = user.email ?: "",
            customerName = name,
            customerPhone = phone,
            customerAddress = address,
            items = cartItems,
            totalAmount = totalAmount,
            orderDate = System.currentTimeMillis(), // Dùng Long cho model
            status = "Pending"
        )

        orderRef.setValue(order).addOnCompleteListener { task ->
            progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                managementCart.clearCart()
                showSuccessDialog()
            } else {
                placeOrderBtn.isEnabled = true
                Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Thành Công")
            .setMessage("Đơn hàng của bạn đã được tiếp nhận!")
            .setCancelable(false)
            .setPositiveButton("Về Trang Chủ") { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }.show()
    }
}
