package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import com.example.cafeapp.R
import com.google.firebase.auth.FirebaseAuth

class AdminMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        findViewById<android.view.View>(R.id.btnManageOrders).setOnClickListener {
            startActivity(Intent(this, AdminOrdersActivity::class.java))
        }

        findViewById<android.view.View>(R.id.btnStatistics).setOnClickListener {
            startActivity(Intent(this, AdminDashboardActivity::class.java))
        }

        findViewById<android.view.View>(R.id.btnLogoutAdmin).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
