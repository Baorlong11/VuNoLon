package com.example.cafeapp.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.cafeapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminDashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        findViewById<android.view.View>(R.id.backBtn).setOnClickListener { finish() }

        calculateStatistics()
    }

    private fun calculateStatistics() {
        val ref = FirebaseDatabase.getInstance().getReference("Orders")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalRevenue = 0.0
                var totalOrders = 0

                for (child in snapshot.children) {
                    val totalAmount = child.child("totalAmount").getValue(Double::class.java) ?: 0.0
                    totalRevenue += totalAmount
                    totalOrders++
                }

                findViewById<TextView>(R.id.totalRevenueTxt).text = "${totalRevenue.toInt()} VND"
                findViewById<TextView>(R.id.totalOrdersTxt).text = totalOrders.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminDashboardActivity, "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
