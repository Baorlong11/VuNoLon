package com.example.cafeapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.cafeapp.Helper.ManagementCart
import com.example.cafeapp.Model.ItemsModel
import com.example.cafeapp.databinding.ViewholderCartBinding

class CartAdapter(
    private val listItemSelected: ArrayList<ItemsModel>,
    private val managementCart: ManagementCart,
    private val listener: ManagementCart.ChangeNumberItemsListener
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItemSelected[position]
        holder.binding.titleCartTxt.text = item.title
        
        val sizeExtra = when (item.size) {
            "M" -> 10000.0
            "L" -> 20000.0
            else -> 0.0
        }
        val pricePerItem = item.price + sizeExtra
        
        holder.binding.feeEachItem.text = "${pricePerItem.toInt()} VND"
        holder.binding.totalEachItem.text = "${(item.numberInCart * pricePerItem).toInt()} VND"
        holder.binding.numberItemTxt.text = item.numberInCart.toString()
        holder.binding.sizeTxt.text = "Size: ${item.size}"

        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
            .into(holder.binding.picCart)

        holder.binding.plusCartBtn.setOnClickListener {
            managementCart.plusItem(listItemSelected, position, object : ManagementCart.ChangeNumberItemsListener {
                override fun onChanged() {
                    notifyDataSetChanged()
                    listener.onChanged()
                }
            })
        }

        holder.binding.minusCartBtn.setOnClickListener {
            managementCart.minusItem(listItemSelected, position, object : ManagementCart.ChangeNumberItemsListener {
                override fun onChanged() {
                    notifyDataSetChanged()
                    listener.onChanged()
                }
            })
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}