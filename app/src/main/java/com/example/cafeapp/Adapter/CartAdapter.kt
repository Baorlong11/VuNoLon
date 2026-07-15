package com.example.cafeapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.cafeapp.Model.CartItem
import com.example.cafeapp.Repository.CartManager
import com.example.cafeapp.databinding.ViewholderItemBinding

class CartAdapter(
    private val items: MutableList<CartItem>,
    private val onCartChanged: () -> Unit // gọi lại khi giỏ hàng thay đổi (để cập nhật tổng tiền)
) : RecyclerView.Adapter<CartAdapter.Viewholder>() {

    private var context: Context? = null

    class Viewholder(val binding: ViewholderItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        context = parent.context
        val binding = ViewholderItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.subtitleTxt.text = "Size: ${item.size}  •  SL: ${item.quantity}"
        holder.binding.priceTxt.text = "${(item.price * item.quantity).toInt()} VND"

        if (item.picUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.picUrl)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(30)))
                .into(holder.binding.imageView4)
        }

        // Nhấn giữ để xoá món khỏi giỏ hàng
        holder.itemView.setOnLongClickListener {
            CartManager.removeItem(holder.itemView.context, item)
            items.removeAt(position)
            notifyItemRemoved(position)
            onCartChanged()
            true
        }
    }

    override fun getItemCount(): Int = items.size
}