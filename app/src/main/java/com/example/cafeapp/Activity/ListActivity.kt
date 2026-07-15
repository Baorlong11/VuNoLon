package com.example.cafeapp.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.adapter.ListItemAdapter
import com.example.cafeapp.viewmodel.MainViewModel

class ListActivity : BaseActivity() {
    private val viewModel = MainViewModel()
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyTxt: TextView
    private lateinit var titleTxt: TextView
    private lateinit var catImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        initViews()
        initList()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.view)
        progressBar = findViewById(R.id.progressBar)
        emptyTxt = findViewById(R.id.emptyTxt)
        titleTxt = findViewById(R.id.titleTxt)
        catImg = findViewById(R.id.catImg)

        findViewById<View>(R.id.menuBtn).setOnClickListener {
            finish() // Quay lại thay vì tạo MainActivity mới (Tối ưu RAM)
        }
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    private fun initList() {
        val title = intent.getStringExtra("title") ?: "Danh mục"
        val categoryId = intent.getIntExtra("id", 0)
        
        titleTxt.text = title
        setCategoryImage(categoryId)

        progressBar.visibility = View.VISIBLE
        emptyTxt.visibility = View.GONE

        // Quan sát dữ liệu từ ViewModel với xử lý lỗi
        viewModel.loadFiltered(categoryId).observe(this) { items ->
            progressBar.visibility = View.GONE
            if (items.isNullOrEmpty()) {
                emptyTxt.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyTxt.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                recyclerView.adapter = ListItemAdapter(items)
            }
        }
    }

    private fun setCategoryImage(id: Int) {
        val drawableId = when (id) {
            1 -> R.drawable.ic_1
            2 -> R.drawable.ic_2
            3 -> R.drawable.ic_3
            4 -> R.drawable.ic_4
            5 -> R.drawable.ic_5
            6 -> R.drawable.ic_6
            7 -> R.drawable.ic_7
            8 -> R.drawable.ic_8
            9 -> R.drawable.ic_9
            else -> 0
        }
        if (drawableId != 0) {
            catImg.setImageDrawable(ContextCompat.getDrawable(this, drawableId))
        }
    }
}
