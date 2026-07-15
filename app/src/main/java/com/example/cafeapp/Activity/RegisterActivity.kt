package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cafeapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Xử lý Window Insets để tránh bị Status Bar đè lên nội dung
        val mainView = findViewById<View>(R.id.main_register) ?: findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.progressBarRegister)

        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val txtLoginLink = findViewById<TextView>(R.id.txtLoginLink)

        btnRegister.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val pass = edtPassword.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                if (pass.length < 6) {
                    Toast.makeText(this, "Mật khẩu phải từ 6 ký tự trở lên", Toast.LENGTH_SHORT).show()
                } else {
                    registerUser(email, pass)
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ Email và Mật khẩu", Toast.LENGTH_SHORT).show()
            }
        }

        txtLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registerUser(email: String, pass: String) {
        progressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid
                saveUserToDatabase(uid, email)
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Đăng ký thất bại: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveUserToDatabase(uid: String?, email: String) {
        if (uid == null) {
            progressBar.visibility = View.GONE
            return
        }

        val role = if (email.equals("admin@cafeapp.com", ignoreCase = true)) "Admin" else "User"
        val userMap = mapOf(
            "uid" to uid,
            "email" to email,
            "role" to role
        )

        FirebaseDatabase.getInstance().getReference("Users").child(uid)
            .setValue(userMap)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Lỗi lưu thông tin: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
