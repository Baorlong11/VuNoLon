package com.example.cafeapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cafeapp.Model.ItemsModel
import com.example.cafeapp.Repository.MainRepository

class MainViewModel : ViewModel() {

    private val repository = MainRepository()

    fun loadItems(): LiveData<MutableList<ItemsModel>> {
        return repository.loadItems()
    }

    fun loadFiltered(id: Int): LiveData<MutableList<ItemsModel>> {
        return repository.loadFiltered(id)
    }
}