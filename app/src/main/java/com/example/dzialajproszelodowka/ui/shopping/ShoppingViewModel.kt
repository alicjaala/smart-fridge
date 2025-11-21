package com.example.dzialajproszelodowka.ui.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dzialajproszelodowka.data.model.ShoppingItem
import com.example.dzialajproszelodowka.data.model.ShoppingList
import com.example.dzialajproszelodowka.data.repository.ShoppingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ShoppingViewModel(private val repository: ShoppingRepository) : ViewModel() {

    val allLists: StateFlow<List<ShoppingList>> = repository.allLists
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    private val _currentListId = MutableStateFlow<Int?>(null)

    val currentListName: StateFlow<String> = _currentListId.map { id ->
        if (id == null) "" else allLists.value.find { it.id == id }?.name ?: ""
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentListItems: StateFlow<List<ShoppingItem>> = _currentListId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.getItemsForList(id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun selectList(listId: Int) {
        _currentListId.value = listId
    }

    fun addList(name: String) {
        viewModelScope.launch {
            repository.insertList(name)
        }
    }

    fun addItemToCurrentList(name: String) {
        val listId = _currentListId.value ?: return
        viewModelScope.launch {
            val newItem = ShoppingItem(listId = listId, name = name, isChecked = false)
            repository.insertItem(newItem)
        }
    }

    fun toggleItem(item: ShoppingItem) {
        viewModelScope.launch {
            val updatedItem = item.copy(isChecked = !item.isChecked)
            repository.updateItem(updatedItem)
        }
    }

    fun deleteCurrentList() {
        val listId = _currentListId.value ?: return
        val listToDelete = allLists.value.find { it.id == listId } ?: return
        viewModelScope.launch {
            repository.deleteList(listToDelete)
        }
    }
}