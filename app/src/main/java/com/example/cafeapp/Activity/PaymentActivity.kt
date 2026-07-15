package com.example.cafeapp.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.cafeapp.Model.ItemsModel
import com.example.cafeapp.Model.OrderModel
import com.example.cafeapp.Repository.CartManager
import com.example.cafeapp.databinding.ActivityPaymentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PaymentActivity : BaseActivity() {
    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)
        binding.txtTotalPayment.text = "${"%,.0f".format(totalPrice)}đ"

        binding.backBtnPayment.setOnClickListener { finish() }

        binding.btnConfirmPayment.setOnClickListener {
            placeOrder(totalPrice)
        }
    }

    private fun placeOrder(totalPrice: Double) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để đặt hàng!", Toast.LENGTH_LONG).show()
            return
        }

        val cartItems = CartManager.getCartItems(this)
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show()
            return
        }

        // Chuyển từng CartItem thành ItemsModel để lưu vào đơn hàng
        val orderItems = ArrayList<ItemsModel>()
        for (cartItem in cartItems) {
            orderItems.add(
                ItemsModel(
                    title = cartItem.title,
                    description = "",
                    picUrl = arrayListOf(cartItem.picUrl),
                    price = cartItem.price * cartItem.quantity,
                    extra = "Size ${cartItem.size} x${cartItem.quantity}",
                    categoryId = ""
                )
            )
        }

        val ref = FirebaseDatabase.getInstance().getReference("Orders")
        val orderId = ref.push().key ?: System.currentTimeMillis().toString()

        val order = OrderModel(
            orderId = orderId,
            userEmail = currentUser.email ?: "",
            items = orderItems,
            totalAmount = totalPrice,
            orderDate = System.currentTimeMillis(),
            status = "Pending"
        )

        binding.btnConfirmPayment.isEnabled = false

        ref.child(orderId).setValue(order)
            .addOnSuccessListener {
                CartManager.clearCart(this)

                val intent = Intent(this, OrderSuccessActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                binding.btnConfirmPayment.isEnabled = true
                Toast.makeText(this, "Lỗi đặt hàng: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}