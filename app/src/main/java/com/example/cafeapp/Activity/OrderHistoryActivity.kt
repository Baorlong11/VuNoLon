package com.example.cafeapp.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.adapter.OrderAdapter
import com.example.cafeapp.model.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OrderHistoryActivity : BaseActivity() {

    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var emptyOrderTxt: TextView
    private lateinit var progressBar: ProgressBar
    private var orderListener: ValueEventListener? = null
    private lateinit var query: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        initViews()
        loadOrderHistory()
    }

    private fun initViews() {
        orderRecyclerView = findViewById(R.id.orderRecyclerView)
        emptyOrderTxt = findViewById(R.id.emptyOrderTxt)
        progressBar = findViewById(R.id.progressBarOrderHistory)
        
        findViewById<ImageView>(R.id.backBtn).setOnClickListener { finish() }
        
        orderRecyclerView.layoutManager = LinearLayoutManager(this)
        orderRecyclerView.setHasFixedSize(true)
    }

    private fun loadOrderHistory() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            emptyOrderTxt.visibility = View.VISIBLE
            emptyOrderTxt.text = "Vui lòng đăng nhập để xem lịch sử"
            return
        }

        progressBar.visibility = View.VISIBLE
        val ref = FirebaseDatabase.getInstance().getReference("Orders")
        query = ref.orderByChild("userEmail").equalTo(user.email)

        orderListener = query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressBar.visibility = View.GONE
                val orderList = mutableListOf<OrderModel>()
                for (child in snapshot.children) {
                    val order = child.getValue(OrderModel::class.java)
                    order?.let { orderList.add(it) }
                }

                if (orderList.isEmpty()) {
                    emptyOrderTxt.visibility = View.VISIBLE
                    orderRecyclerView.visibility = View.GONE
                } else {
                    emptyOrderTxt.visibility = View.GONE
                    orderRecyclerView.visibility = View.VISIBLE
                    orderRecyclerView.adapter = OrderAdapter(orderList.asReversed())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@OrderHistoryActivity, "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        orderListener?.let { query.removeEventListener(it) }
    }
}
