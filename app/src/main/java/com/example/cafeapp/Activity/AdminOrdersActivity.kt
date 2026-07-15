package com.example.cafeapp.activity

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.adapter.OrderAdapter
import com.example.cafeapp.model.OrderModel
import com.google.firebase.database.*

class AdminOrdersActivity : BaseActivity() {

    private var ordersListener: ValueEventListener? = null
    private lateinit var ordersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_orders)

        findViewById<android.view.View>(R.id.backBtn).setOnClickListener { finish() }
        
        val recyclerView = findViewById<RecyclerView>(R.id.adminOrderRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true) // Tối ưu: Báo cho hệ thống kích thước item cố định

        loadAllOrders()
    }

    private fun loadAllOrders() {
        ordersRef = FirebaseDatabase.getInstance().getReference("Orders")
        
        // Tối ưu: Lấy 50 đơn hàng mới nhất thay vì tải toàn bộ lịch sử
        val query = ordersRef.orderByChild("orderDate").limitToLast(50)

        ordersListener = query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = mutableListOf<OrderModel>()
                for (child in snapshot.children) {
                    val order = child.getValue(OrderModel::class.java)
                    order?.let { orderList.add(it) }
                }

                if (orderList.isEmpty()) {
                    Toast.makeText(this@AdminOrdersActivity, "Không có đơn hàng nào", Toast.LENGTH_SHORT).show()
                } else {
                    val recyclerView = findViewById<RecyclerView>(R.id.adminOrderRecyclerView)
                    // reversed() giúp đơn hàng mới nhất hiện lên trên cùng
                    recyclerView?.adapter = OrderAdapter(orderList.reversed())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminOrdersActivity, "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // Quan trọng: Gỡ bỏ listener để tránh Memory Leak khi thoát Activity
        ordersListener?.let { ordersRef.removeEventListener(it) }
    }
}
