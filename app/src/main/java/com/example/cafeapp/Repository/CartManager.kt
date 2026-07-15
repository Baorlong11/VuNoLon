package com.example.cafeapp.Repository

import android.content.Context
import com.example.cafeapp.Model.CartItem
import org.json.JSONArray
import org.json.JSONObject

object CartManager {
    private const val PREF_NAME = "cart_prefs"
    private const val KEY_CART = "cart_items"

    fun getCartItems(context: Context): MutableList<CartItem> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_CART, null) ?: return mutableListOf()

        val list = mutableListOf<CartItem>()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                CartItem(
                    title = obj.getString("title"),
                    price = obj.getDouble("price"),
                    picUrl = obj.getString("picUrl"),
                    size = obj.getString("size"),
                    quantity = obj.getInt("quantity")
                )
            )
        }
        return list
    }

    private fun saveCartItems(context: Context, items: List<CartItem>) {
        val array = JSONArray()
        for (item in items) {
            val obj = JSONObject()
            obj.put("title", item.title)
            obj.put("price", item.price)
            obj.put("picUrl", item.picUrl)
            obj.put("size", item.size)
            obj.put("quantity", item.quantity)
            array.put(obj)
        }
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CART, array.toString()).apply()
    }

    fun addToCart(context: Context, newItem: CartItem) {
        val items = getCartItems(context)

        // Nếu đã có món cùng tên + cùng size -> tăng số lượng thay vì thêm dòng mới
        val existing = items.find { it.title == newItem.title && it.size == newItem.size }
        if (existing != null) {
            existing.quantity += newItem.quantity
        } else {
            items.add(newItem)
        }

        saveCartItems(context, items)
    }

    fun removeItem(context: Context, item: CartItem) {
        val items = getCartItems(context)
        items.removeAll { it.title == item.title && it.size == item.size }
        saveCartItems(context, items)
    }

    fun clearCart(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_CART).apply()
    }

    fun getTotalPrice(context: Context): Double {
        return getCartItems(context).sumOf { it.price * it.quantity }
    }
}