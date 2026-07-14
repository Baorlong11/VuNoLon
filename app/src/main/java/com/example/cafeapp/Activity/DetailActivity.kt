package com.example.cafeapp.Activity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.cafeapp.Model.ItemsModel
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityDetailBinding

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var context: Context

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

                // Đổ ảnh có lệnh bảo vệ
                if (item.picUrl.isNotEmpty()) {
                    Glide.with(context)
                        .load(item.picUrl[0])
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
                        .into(binding.img)
                }

                backBtn.setOnClickListener { finish() }

                sizeBtn1.setOnClickListener {
                    sizeBtn1.setBackgroundResource(R.drawable.orange_stroke_bg)
                    sizeBtn2.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn3.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn1.setTextColor(ContextCompat.getColor(context, R.color.orange))
                    sizeBtn2.setTextColor(ContextCompat.getColor(context, R.color.white))
                    sizeBtn3.setTextColor(ContextCompat.getColor(context, R.color.white))
                }

                sizeBtn2.setOnClickListener {
                    sizeBtn1.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn2.setBackgroundResource(R.drawable.orange_stroke_bg)
                    sizeBtn3.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn1.setTextColor(ContextCompat.getColor(context, R.color.white))
                    sizeBtn2.setTextColor(ContextCompat.getColor(context, R.color.orange))
                    sizeBtn3.setTextColor(ContextCompat.getColor(context, R.color.white))
                }

                sizeBtn3.setOnClickListener {
                    sizeBtn1.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn2.setBackgroundResource(R.drawable.dark_grey_bg2)
                    sizeBtn3.setBackgroundResource(R.drawable.orange_stroke_bg)
                    sizeBtn1.setTextColor(ContextCompat.getColor(context, R.color.white))
                    sizeBtn2.setTextColor(ContextCompat.getColor(context, R.color.white))
                    sizeBtn3.setTextColor(ContextCompat.getColor(context, R.color.orange))
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this@DetailActivity, "Gặp lỗi: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}