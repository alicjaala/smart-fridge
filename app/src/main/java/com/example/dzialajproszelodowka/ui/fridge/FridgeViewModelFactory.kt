package com.example.dzialajproszelodowka.ui.fridge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dzialajproszelodowka.data.repository.ProductRepository

class FridgeViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(FridgeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FridgeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow Viewmodel class")
    }
}
