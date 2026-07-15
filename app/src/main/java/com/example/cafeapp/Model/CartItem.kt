package com.example.cafeapp.Model

data class CartItem(
    var title: String = "",
    var price: Double = 0.0,
    var picUrl: String = "",
    var size: String = "S",
    var quantity: Int = 1
)