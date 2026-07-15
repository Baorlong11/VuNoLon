package com.example.cafeapp.model

import java.io.Serializable

data class OrderModel(
    var orderId: String = "",
    var userEmail: String = "",
    var customerName: String = "",
    var customerPhone: String = "",
    var customerAddress: String = "",
    var items: ArrayList<ItemsModel> = ArrayList(),
    var totalAmount: Double = 0.0,
    var orderDate: Long = 0,
    var status: String = "Pending"
) : Serializable
