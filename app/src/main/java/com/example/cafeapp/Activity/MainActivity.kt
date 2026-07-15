package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cafeapp.R
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enableEdgeToEdge()

        val mainView = findViewById<android.view.View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setBlurEffect()
        setVariable()
    }

    private fun setVariable() {
        findViewById<LinearLayout>(R.id.btn1).setOnClickListener { startListActivity(1, "Ice Drink") }
        findViewById<LinearLayout>(R.id.btn2).setOnClickListener { startListActivity(2, "Hot Drink") }
        findViewById<LinearLayout>(R.id.btn3).setOnClickListener { startListActivity(3, "Hot Coffee") }
        findViewById<LinearLayout>(R.id.btn4).setOnClickListener { startListActivity(4, "Ice Coffee") }
        findViewById<LinearLayout>(R.id.btn5).setOnClickListener { startListActivity(5, "Brewing Coffee") }
        findViewById<LinearLayout>(R.id.btn6).setOnClickListener { startListActivity(6, "Shake") }
        findViewById<LinearLayout>(R.id.btn7).setOnClickListener { startListActivity(7, "Restaurant") }
        findViewById<LinearLayout>(R.id.btn8).setOnClickListener { startListActivity(8, "Breakfast") }
        findViewById<LinearLayout>(R.id.btn9).setOnClickListener { startListActivity(9, "Cake") }

        findViewById<ImageView>(R.id.btnHistory).setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }

        findViewById<ImageView>(R.id.btnCart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        findViewById<ImageView>(R.id.btnLogout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
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
        val blurView = findViewById<BlurView>(R.id.blurView)

        blurView.setupWith(
            rootView,
            RenderScriptBlur(this)
        )
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)

        blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        blurView.clipToOutline = true
    }
}
