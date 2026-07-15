package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.cafeapp.R
import com.example.cafeapp.helper.ManagementCart
import com.example.cafeapp.model.ItemsModel

class DetailActivity : BaseActivity() {
    private lateinit var item: ItemsModel
    private var numberOrder = 1
    private var isFavorite = false
    private lateinit var managementCart: ManagementCart
    
    private lateinit var priceTxt: TextView
    private lateinit var numberOrderTxt: TextView
    private lateinit var sizeBtn1: Button
    private lateinit var sizeBtn2: Button
    private lateinit var sizeBtn3: Button
    private lateinit var favBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        
        managementCart = ManagementCart(this)
        initViews()
        bundle()
        initFavorite()
        initQuantity()
    }

    private fun initViews() {
        priceTxt = findViewById(R.id.priceTxt)
        numberOrderTxt = findViewById(R.id.numberOrderTxt)
        sizeBtn1 = findViewById(R.id.sizeBtn1)
        sizeBtn2 = findViewById(R.id.sizeBtn2)
        sizeBtn3 = findViewById(R.id.sizeBtn3)
        favBtn = findViewById(R.id.favBtn)
    }

    private fun initFavorite() {
        favBtn.setOnClickListener {
            isFavorite = !isFavorite
            favBtn.setColorFilter(ContextCompat.getColor(this, if (isFavorite) R.color.red else R.color.white))
            Toast.makeText(this, if (isFavorite) "Đã thích" else "Bỏ thích", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initQuantity() {
        findViewById<View>(R.id.plusBtn).setOnClickListener {
            numberOrder++
            numberOrderTxt.text = numberOrder.toString()
            updatePrice()
        }

        findViewById<View>(R.id.minusBtn).setOnClickListener {
            if (numberOrder > 1) {
                numberOrder--
                numberOrderTxt.text = numberOrder.toString()
                updatePrice()
            }
        }
    }

    private fun updatePrice() {
        val sizeExtra = when (item.size) {
            "M" -> 10000.0
            "L" -> 20000.0
            else -> 0.0
        }
        val totalPrice = (item.price + sizeExtra) * numberOrder
        priceTxt.text = "${totalPrice.toInt()} VND"
    }

    private fun bundle() {
        try {
            item = intent.getParcelableExtra("object") ?: return
            item.size = "S"

            findViewById<TextView>(R.id.titleTxt).text = item.title
            findViewById<TextView>(R.id.subtitleTxt).text = item.extra
            findViewById<TextView>(R.id.descriptionTxt).text = item.description
            updatePrice()

            val img = findViewById<ImageView>(R.id.img)
            if (item.picUrl.isNotEmpty()) {
                Glide.with(this)
                    .load(item.picUrl[0])
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
                    .into(img)
            }

            findViewById<View>(R.id.backBtn).setOnClickListener { finish() }

            sizeBtn1.setOnClickListener { selectSize("S") }
            sizeBtn2.setOnClickListener { selectSize("M") }
            sizeBtn3.setOnClickListener { selectSize("L") }

            // FIX: Đảm bảo OnClickListener được gán chính xác
            findViewById<Button>(R.id.addToCartBtn).setOnClickListener {
                item.numberInCart = numberOrder
                managementCart.insertItem(item)
            }

            findViewById<Button>(R.id.buyNowBtn).setOnClickListener {
                item.numberInCart = numberOrder
                managementCart.insertItem(item)
                Toast.makeText(this, "Đang mở giỏ hàng...", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, CartActivity::class.java))
            }
            
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun selectSize(size: String) {
        item.size = size
        val orange = ContextCompat.getColor(this, R.color.orange)
        val white = ContextCompat.getColor(this, R.color.white)

        sizeBtn1.apply {
            setBackgroundResource(if (size == "S") R.drawable.orange_stroke_bg else R.drawable.dark_grey_bg2)
            setTextColor(if (size == "S") orange else white)
        }
        sizeBtn2.apply {
            setBackgroundResource(if (size == "M") R.drawable.orange_stroke_bg else R.drawable.dark_grey_bg2)
            setTextColor(if (size == "M") orange else white)
        }
        sizeBtn3.apply {
            setBackgroundResource(if (size == "L") R.drawable.orange_stroke_bg else R.drawable.dark_grey_bg2)
            setTextColor(if (size == "L") orange else white)
        }
        updatePrice()
    }
}
