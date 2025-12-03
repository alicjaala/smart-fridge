package com.example.dzialajproszelodowka.ui.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dzialajproszelodowka.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _searchResultJson = MutableStateFlow("Enter keywords to search for a recipe...")
    val searchResultJson: StateFlow<String> = _searchResultJson

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun searchRecipes(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            _searchResultJson.value = "Searching..."

            val result = repository.searchRecipes(query)

            _searchResultJson.value = result
            _isLoading.value = false
        }
    }

    fun resetSearchState() {
        _searchResultJson.value = "Enter keywords to search for a recipe..."
        _isLoading.value = false
    }
}