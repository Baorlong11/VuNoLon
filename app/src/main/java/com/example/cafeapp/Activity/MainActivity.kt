package com.example.cafeapp.Activity

import android.content.Intent // BẮT BUỘC phải có import này
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cafeapp.R
import eightbitlab.com.blurview.RenderScriptBlur
import com.example.cafeapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setBlurEffect()
        setVariable()
    }

    private fun setVariable() {
        binding.apply {
            btn1.setOnClickListener { startListActivity(1, "Ice Drink") }
            btn2.setOnClickListener { startListActivity(2, "Hot Drink") }
            btn3.setOnClickListener { startListActivity(3, "Hot Coffee") }
            btn4.setOnClickListener { startListActivity(4, "Ice Coffee") }
            btn5.setOnClickListener { startListActivity(5, "Brewing Coffee") }
            btn6.setOnClickListener { startListActivity(6, "Shake") }
            btn7.setOnClickListener { startListActivity(7, "Restaurant") }
            btn8.setOnClickListener { startListActivity(8, "Breakfast") }
            btn9.setOnClickListener { startListActivity(9, "Cake") }

            // FIX: Đắp sự kiện click cho nút Lịch Sử chuẩn quy trình binding nhóm
            btnHistory.setOnClickListener {
                val intent = Intent(this@MainActivity, OrderHistoryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun startListActivity(id: Int, title: String) {
        val intent = Intent(this, ListActivity::class.java)

        intent.putExtra("id", id)
        intent.putExtra("title", title)

        startActivity(intent)
    }

    private fun setBlurEffect() {
        val radius = 10f
        val decorView = this.window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowBackground = decorView.background

        binding.blurView.setupWith(
            rootView,
            RenderScriptBlur(this)
        )
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)

        binding.blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        binding.blurView.clipToOutline = true
    }
}