package com.example.cafeapp.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafeapp.Adapter.OrderAdapter
import com.example.cafeapp.Model.OrderModel
import com.example.cafeapp.databinding.ActivityOrderHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class OrderHistoryActivity : BaseActivity() { // Đổi thành BaseActivity nếu project của bạn dùng chung BaseActivity
    private lateinit var binding: ActivityOrderHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }

        binding.orderRecyclerView.layoutManager = LinearLayoutManager(this)

        loadOrderHistory()
    }

    private fun loadOrderHistory() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            binding.emptyOrderTxt.visibility = View.VISIBLE
            binding.emptyOrderTxt.text = "Vui lòng đăng nhập để xem lịch sử!"
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("Orders")
        val query = ref.orderByChild("userEmail").equalTo(currentUser.email)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = ArrayList<OrderModel>()

                for (childSnapshot in snapshot.children) {
                    val order = childSnapshot.getValue(OrderModel::class.java)
                    order?.let { orderList.add(it) }
                }

                orderList.sortByDescending { it.orderDate }

                if (orderList.isEmpty()) {
                    binding.emptyOrderTxt.visibility = View.VISIBLE
                    binding.orderRecyclerView.visibility = View.GONE
                } else {
                    binding.emptyOrderTxt.visibility = View.GONE
                    binding.orderRecyclerView.visibility = View.VISIBLE
                    binding.orderRecyclerView.adapter = OrderAdapter(orderList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.emptyOrderTxt.visibility = View.VISIBLE
                binding.emptyOrderTxt.text = "Lỗi tải dữ liệu đơn hàng!"
            }
        })
    }
}