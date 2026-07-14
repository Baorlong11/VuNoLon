package com.example.cafeapp.Model

import java.io.Serializable

data class OrderModel(
    var orderId: String = "",
    var userEmail: String = "",
    var items: ArrayList<ItemsModel> = ArrayList(), // Sửa List thành ArrayList ở đây
    var totalAmount: Double = 0.0,
    var orderDate: Long = 0,
    var status: String = "Pending"
) : Serializable