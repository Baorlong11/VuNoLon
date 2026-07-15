package com.example.cafeapp.Activity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.cafeapp.Helper.ManagementCart
import com.example.cafeapp.Model.ItemsModel
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityDetailBinding

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var context: Context
    private var numberOrder = 1
    private var isFavorite = false
    private lateinit var managementCart: ManagementCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        managementCart = ManagementCart(this)

        bundle()
        initFavorite()
        initQuantity()
    }

    private fun initFavorite() {
        binding.favBtn.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                binding.favBtn.setColorFilter(ContextCompat.getColor(context, R.color.red))
            } else {
                binding.favBtn.setColorFilter(ContextCompat.getColor(context, R.color.white))
            }
        }
    }

    private fun initQuantity() {
        binding.plusBtn.setOnClickListener {
            numberOrder++
            binding.numberOrderTxt.text = numberOrder.toString()
            updatePrice()
        }

        binding.minusBtn.setOnClickListener {
            if (numberOrder > 1) {
                numberOrder--
                binding.numberOrderTxt.text = numberOrder.toString()
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
        binding.priceTxt.text = "${totalPrice.toInt()} VND"
    }

    private fun bundle() {
        try {
            binding.apply {
                item = intent.getParcelableExtra("object")!!
                item.size = "S" // Mặc định size S

                titleTxt.text = item.title
                subtitleTxt.text = item.extra
                descriptionTxt.text = item.description
                updatePrice()

                // Đổ ảnh có lệnh bảo vệ
                if (item.picUrl.isNotEmpty()) {
                    Glide.with(context)
                        .load(item.picUrl[0])
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
                        .into(binding.img)
                }

                backBtn.setOnClickListener { finish() }

                sizeBtn1.setOnClickListener {
                    item.size = "S"
                    sizeBtn1.setBackgroundResource(R.drawable.orange_stroke_bg)
                    sizeBtn2.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn3.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn1.setTextColor(ContextCompat.getColor(context, R.color.orange))
                    sizeBtn2.setTextColor(ContextCompat.getColor(context, R.color.white))
                    sizeBtn3.setTextColor(ContextCompat.getColor(context, R.color.white))
                    updatePrice()
                }

                sizeBtn2.setOnClickListener {
                    item.size = "M"
                    sizeBtn1.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn2.setBackgroundResource(R.drawable.orange_stroke_bg)
                    sizeBtn3.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn1.setTextColor(ContextCompat.getColor(context, R.color.white))
                    sizeBtn2.setTextColor(ContextCompat.getColor(context, R.color.orange))
                    sizeBtn3.setTextColor(ContextCompat.getColor(context, R.color.white))
                    updatePrice()
                }

                sizeBtn3.setOnClickListener {
                    item.size = "L"
                    sizeBtn1.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn2.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn3.setBackgroundResource(R.drawable.orange_stroke_bg)
                    sizeBtn1.setTextColor(ContextCompat.getColor(context, R.color.white))
                    sizeBtn2.setTextColor(ContextCompat.getColor(context, R.color.white))
                    sizeBtn3.setTextColor(ContextCompat.getColor(context, R.color.orange))
                    updatePrice()
                }

                buyBtn.setOnClickListener {
                    item.numberInCart = numberOrder
                    managementCart.insertItem(item)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this@DetailActivity, "Gặp lỗi: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}