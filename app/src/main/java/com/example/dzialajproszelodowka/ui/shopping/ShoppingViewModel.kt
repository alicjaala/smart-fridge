package com.example.dzialajproszelodowka.ui.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dzialajproszelodowka.data.model.ShoppingList
import com.example.dzialajproszelodowka.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShoppingViewModel(private val repository: ShoppingRepository) : ViewModel() {

    val allLists: StateFlow<List<ShoppingList>> = repository.allLists
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addList(name: String) {
        viewModelScope.launch {
            repository.insertList(name)
        }
    }

    fun deleteList(shoppingList: ShoppingList) {
        viewModelScope.launch {
            repository.deleteList(shoppingList)
        }
    }
}