package com.example.cafeapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cafeapp.model.ItemsModel
import com.google.firebase.database.*

class MainRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    // Tối ưu: Sử dụng listener lâu dài cho trang chủ để cập nhật giá ngay lập tức
    fun loadItems(): LiveData<MutableList<ItemsModel>> {
        val listData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = firebaseDatabase.getReference("Items")
        
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(ItemsModel::class.java)
                    item?.let { lists.add(it) }
                }
                listData.postValue(lists) // postValue an toàn hơn khi gọi từ background thread
            }

            override fun onCancelled(error: DatabaseError) {
                listData.postValue(mutableListOf())
            }
        })
        return listData
    }

    fun loadFiltered(id: Int): LiveData<MutableList<ItemsModel>> {
        val listData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = firebaseDatabase.getReference("Items")

        // Tối ưu: Firebase query đôi khi cần string, đôi khi cần int. Chúng ta thử cả hai.
        val query: Query = ref.orderByChild("categoryId").equalTo(id.toDouble())

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(ItemsModel::class.java)
                        item?.let { lists.add(it) }
                    }
                    listData.postValue(lists)
                } else {
                    // Fallback: Nếu không tìm thấy theo Number, thử tìm theo String (do data Firebase có thể hỗn hợp)
                    ref.orderByChild("categoryId").equalTo(id.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(subSnapshot: DataSnapshot) {
                            val subLists = mutableListOf<ItemsModel>()
                            for (child in subSnapshot.children) {
                                val item = child.getValue(ItemsModel::class.java)
                                item?.let { subLists.add(it) }
                            }
                            listData.postValue(subLists)
                        }
                        override fun onCancelled(error: DatabaseError) {
                            listData.postValue(mutableListOf())
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                listData.postValue(mutableListOf())
            }
        })
        return listData
    }
}
