package com.example.cafeapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cafeapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Xử lý Window Insets để tránh bị Status Bar đè lên (do BaseActivity dùng FLAG_LAYOUT_NO_LIMITS)
        val mainView = findViewById<View>(R.id.main_login) ?: findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.progressBarLogin)

        val edtEmail = findViewById<EditText>(R.id.edtEmailLogin)
        val edtPassword = findViewById<EditText>(R.id.edtPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtRegisterLink = findViewById<TextView>(R.id.txtRegisterLink)

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val pass = edtPassword.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                loginUser(email, pass)
            } else {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show()
            }
        }

        txtRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser(email: String, pass: String) {
        progressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                checkUserRole(email)
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Đăng nhập thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkUserRole(email: String) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            progressBar.visibility = View.GONE
            return
        }

        if (email.equals("admin@cafeapp.com", ignoreCase = true)) {
            saveRoleAndNavigate("Admin")
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.uid)
        ref.child("role").get().addOnSuccessListener { snapshot ->
            val role = snapshot.getValue(String::class.java) ?: "User"
            saveRoleAndNavigate(role)
        }.addOnFailureListener {
            saveRoleAndNavigate("User")
        }
    }

    private fun saveRoleAndNavigate(role: String) {
        progressBar.visibility = View.GONE
        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPref.edit().putString("user_role", role).apply()

        val intent = if (role == "Admin") {
            Intent(this, AdminMainActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user != null) {
            val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val cachedRole = sharedPref.getString("user_role", null)
            
            if (cachedRole != null) {
                val intent = if (cachedRole == "Admin") {
                    Intent(this, AdminMainActivity::class.java)
                } else {
                    Intent(this, MainActivity::class.java)
                }
                startActivity(intent)
                finish()
            } else {
                checkUserRole(user.email ?: "")
            }
        }
    }
}
