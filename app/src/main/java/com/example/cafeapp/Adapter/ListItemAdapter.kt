package com.example.cafeapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.cafeapp.activity.DetailActivity
import com.example.cafeapp.model.ItemsModel
import com.example.cafeapp.databinding.ViewholderItemBinding

class ListItemAdapter(val items: MutableList<ItemsModel>) : RecyclerView.Adapter<ListItemAdapter.Viewholder>() {

    private var context: Context? = null

    class Viewholder(val binding: ViewholderItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: ListItemAdapter.Viewholder, position: Int) {
        val item = items[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.subtitleTxt.text = item.description

        holder.binding.priceTxt.text = "${item.price.toInt()} VND"
        if (item.picUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.picUrl[0])
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(30)))
                .into(holder.binding.imageView4)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("object", item)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}