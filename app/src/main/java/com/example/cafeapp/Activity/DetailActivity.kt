package com.example.cafeapp.Activity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.cafeapp.Model.CartItem
import com.example.cafeapp.Model.ItemsModel
import com.example.cafeapp.R
import com.example.cafeapp.Repository.CartManager
import com.example.cafeapp.databinding.ActivityDetailBinding

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var context: Context
    private var selectedSize: String = "S"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        bundle()
    }

    private fun bundle() {
        try {
            binding.apply {
                item = intent.getParcelableExtra("object")!!

                titleTxt.text = item.title
                subtitleTxt.text = item.extra
                descriptionTxt.text = item.description
                priceTxt.text = "${item.price.toInt()} VND"

                if (item.picUrl.isNotEmpty()) {
                    Glide.with(context)
                        .load(item.picUrl[0])
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
                        .into(binding.img)
                }

                backBtn.setOnClickListener { finish() }

                sizeBtn1.setOnClickListener {
                    selectedSize = "S"
                    sizeBtn1.setBackgroundResource(R.drawable.orange_stroke_bg)
                    sizeBtn2.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn3.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn1.setTextColor(ContextCompat.getColor(context, R.color.orange))
                    sizeBtn2.setTextColor(ContextCompat.getColor(context, R.color.white))
                    sizeBtn3.setTextColor(ContextCompat.getColor(context, R.color.white))
                }

                sizeBtn2.setOnClickListener {
                    selectedSize = "M"
                    sizeBtn1.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn2.setBackgroundResource(R.drawable.orange_stroke_bg)
                    sizeBtn3.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn1.setTextColor(ContextCompat.getColor(context, R.color.white))
                    sizeBtn2.setTextColor(ContextCompat.getColor(context, R.color.orange))
                    sizeBtn3.setTextColor(ContextCompat.getColor(context, R.color.white))
                }

                sizeBtn3.setOnClickListener {
                    selectedSize = "L"
                    sizeBtn1.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn2.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn3.setBackgroundResource(R.drawable.orange_stroke_bg)
                    sizeBtn1.setTextColor(ContextCompat.getColor(context, R.color.white))
                    sizeBtn2.setTextColor(ContextCompat.getColor(context, R.color.white))
                    sizeBtn3.setTextColor(ContextCompat.getColor(context, R.color.orange))
                }

                // Thêm vào giỏ hàng
                buyBtn.setOnClickListener {
                    val cartItem = CartItem(
                        title = item.title,
                        price = item.price,
                        picUrl = if (item.picUrl.isNotEmpty()) item.picUrl[0] else "",
                        size = selectedSize,
                        quantity = 1
                    )
                    CartManager.addToCart(context, cartItem)
                    Toast.makeText(context, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this@DetailActivity, "Gặp lỗi: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}