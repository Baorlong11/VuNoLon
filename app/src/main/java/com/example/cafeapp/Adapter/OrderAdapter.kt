package com.example.cafeapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.model.OrderModel
import com.example.cafeapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderAdapter(private val orderList: List<OrderModel>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderIdTxt: TextView = itemView.findViewById(R.id.orderIdTxt)
        val statusTxt: TextView = itemView.findViewById(R.id.statusTxt)
        val dateTxt: TextView = itemView.findViewById(R.id.dateTxt)
        val itemCountTxt: TextView = itemView.findViewById(R.id.itemCountTxt)
        val totalPriceTxt: TextView = itemView.findViewById(R.id.totalPriceTxt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        holder.orderIdTxt.text = "Mã đơn: #${order.orderId.takeLast(6).uppercase()}"
        holder.statusTxt.text = order.status
        holder.totalPriceTxt.text = "${order.totalAmount.toInt()} VND"
        holder.itemCountTxt.text = "${order.items.size} sản phẩm"

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.dateTxt.text = "Ngày đặt: ${sdf.format(Date(order.orderDate))}"

        when (order.status.lowercase()) {
            "pending" -> holder.statusTxt.setTextColor(Color.parseColor("#FF9800"))
            "completed" -> holder.statusTxt.setTextColor(Color.parseColor("#4CAF50"))
            else -> holder.statusTxt.setTextColor(Color.parseColor("#2196F3"))
        }
    }

    override fun getItemCount(): Int = orderList.size
}