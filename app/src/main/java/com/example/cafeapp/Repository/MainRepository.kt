package com.example.cafeapp.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cafeapp.Model.ItemsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    fun loadItems(): LiveData<MutableList<ItemsModel>> {
        val listData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = firebaseDatabase.getReference("Items")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val lists = mutableListOf<ItemsModel>()
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(ItemsModel::class.java)
                        item?.let { lists.add(it) }
                    }
                    listData.value = lists
                } catch (e: Exception) {
                    // In lỗi ra Logcat nếu bị sai kiểu dữ liệu
                    Log.e("FirebaseDebug", "Lỗi loadItems: ${e.message}")
                    // Ép trả về danh sách rỗng để ứng dụng TẮT VÒNG XOAY
                    listData.value = mutableListOf()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseDebug", "Lỗi mạng: ${error.message}")
                listData.value = mutableListOf()
            }
        })
        return listData
    }

    fun loadFiltered(id: Int): LiveData<MutableList<ItemsModel>> {
        val listData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = firebaseDatabase.getReference("Items")

        val query: Query = ref.orderByChild("categoryId").equalTo(id.toString())

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val lists = mutableListOf<ItemsModel>()

                    if (!snapshot.exists()) {
                        Log.d("FirebaseDebug", "Không tìm thấy dữ liệu trên Firebase cho ID: $id")
                    }

                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(ItemsModel::class.java)
                        item?.let { lists.add(it) }
                    }
                    listData.value = lists
                    Log.d("FirebaseDebug", "Đã tải thành công ${lists.size} sản phẩm")

                } catch (e: Exception) {
                    Log.e("FirebaseDebug", "Lỗi parse dữ liệu loadFiltered: ${e.message}")
                    listData.value = mutableListOf()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseDebug", "Bị hủy do lỗi: ${error.message}")
                listData.value = mutableListOf()
            }
        })
        return listData
    }
}