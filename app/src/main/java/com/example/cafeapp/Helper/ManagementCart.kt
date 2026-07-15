package com.example.cafeapp.Helper

import android.content.Context
import android.widget.Toast
import com.example.cafeapp.Model.ItemsModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ManagementCart(val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("cafe_cart", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun insertItem(item: ItemsModel) {
        val listCart = getListCart()
        val existInCart = listCart.any { it.title == item.title && it.size == item.size }

        if (existInCart) {
            listCart.forEach {
                if (it.title == item.title && it.size == item.size) {
                    it.numberInCart += item.numberInCart
                }
            }
        } else {
            listCart.add(item)
        }

        sharedPreferences.edit().putString("cart_list", gson.toJson(listCart)).apply()
        Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): ArrayList<ItemsModel> {
        val json = sharedPreferences.getString("cart_list", null)
        if (json == null) {
            return ArrayList()
        }
        val type = object : TypeToken<ArrayList<ItemsModel>>() {}.type
        return gson.fromJson(json, type)
    }

    fun minusItem(listCart: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        if (listCart[position].numberInCart == 1) {
            listCart.removeAt(position)
        } else {
            listCart[position].numberInCart--
        }
        sharedPreferences.edit().putString("cart_list", gson.toJson(listCart)).apply()
        listener.onChanged()
    }

    fun plusItem(listCart: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        listCart[position].numberInCart++
        sharedPreferences.edit().putString("cart_list", gson.toJson(listCart)).apply()
        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val listCart = getListCart()
        var fee = 0.0
        for (item in listCart) {
            val sizeExtra = when (item.size) {
                "M" -> 10000.0
                "L" -> 20000.0
                else -> 0.0
            }
            fee += (item.price + sizeExtra) * item.numberInCart
        }
        return fee
    }

    interface ChangeNumberItemsListener {
        fun onChanged()
    }
}