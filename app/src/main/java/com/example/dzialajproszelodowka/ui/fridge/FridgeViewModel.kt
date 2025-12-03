package com.example.dzialajproszelodowka.ui.fridge

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dzialajproszelodowka.data.model.Product
import com.example.dzialajproszelodowka.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class FridgeViewModel(private val repository: ProductRepository): ViewModel() {


    val allProduct: StateFlow<List<Product>> = repository.allProducts.catch {
            e ->
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    fun addProduct(name: String, amount: Int, expiryDate: Date) {
        viewModelScope.launch {
            val newProduct = Product(name = name, amount = amount, expiryDate = expiryDate)
            repository.insertProduct(newProduct)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product);
        }
    }
}