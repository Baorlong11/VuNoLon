package com.example.cafeapp.helper

import android.content.Context
import android.widget.Toast
import com.example.cafeapp.model.ItemsModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ManagementCart(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("cafe_cart", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Tối ưu: Đồng bộ hóa luồng dữ liệu để tránh ghi đè sai
    fun insertItem(item: ItemsModel) {
        val listCart = getListCart()
        val existingItem = listCart.find { it.title == item.title && it.size == item.size }

        if (existingItem != null) {
            existingItem.numberInCart += item.numberInCart
        } else {
            listCart.add(item)
        }

        saveCart(listCart)
        Toast.makeText(context, "Đã thêm ${item.title} vào giỏ", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): ArrayList<ItemsModel> {
        val json = sharedPreferences.getString("cart_list", null)
        if (json.isNullOrEmpty()) return ArrayList()
        
        return try {
            val listType: Type = object : TypeToken<ArrayList<ItemsModel>>() {}.type
            gson.fromJson(json, listType) ?: ArrayList()
        } catch (e: Exception) {
            ArrayList() // Trả về danh sách trống nếu dữ liệu lỗi
        }
    }

    private fun saveCart(listCart: ArrayList<ItemsModel>) {
        sharedPreferences.edit().putString("cart_list", gson.toJson(listCart)).apply()
    }

    fun minusItem(listCart: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        if (position < 0 || position >= listCart.size) return
        
        if (listCart[position].numberInCart <= 1) {
            listCart.removeAt(position)
        } else {
            listCart[position].numberInCart--
        }
        saveCart(listCart)
        listener.onChanged()
    }

    fun plusItem(listCart: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        if (position < 0 || position >= listCart.size) return
        
        listCart[position].numberInCart++
        saveCart(listCart)
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

    fun clearCart() {
        sharedPreferences.edit().remove("cart_list").apply()
    }

    interface ChangeNumberItemsListener {
        fun onChanged()
    }
}
