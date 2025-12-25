package com.example.dzialajproszelodowka.ui.recipe

import androidx.compose.remote.creation.first
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dzialajproszelodowka.data.model.RecipeDto
import com.example.dzialajproszelodowka.data.model.ShoppingItem
import com.example.dzialajproszelodowka.data.repository.ProductRepository
import com.example.dzialajproszelodowka.data.repository.RecipeRepository
import com.example.dzialajproszelodowka.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val recipeRepository: RecipeRepository,
    private val shoppingRepository: ShoppingRepository,
    private val productRepository: ProductRepository
    ) : ViewModel() {

    private val _searchResultJson = MutableStateFlow("Enter keywords to search for a recipe...")
    val searchResultJson: StateFlow<String> = _searchResultJson

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun searchRecipes(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            _searchResultJson.value = "Searching..."

            val result = recipeRepository.searchRecipes(query)

            _searchResultJson.value = result
            _isLoading.value = false
        }
    }

    fun searchWithFridgeIngredients() {
        viewModelScope.launch {
            _isLoading.value = true
            _searchResultJson.value = "Checking your fridge..."

            val products = productRepository.allProducts.first()

            if (products.isEmpty()) {
                _searchResultJson.value = "Your fridge is empty :("
                _isLoading.value = false
                return@launch
            }

            val ingredientsQuery = products.take(10).joinToString(",") {it.name}

            _searchResultJson.value = "Searching recipes with your ingredients..."
            val result = recipeRepository.searchRecipes(ingredientsQuery)

            _searchResultJson.value = result
            _isLoading.value = false
        }
    }

    fun resetSearchState() {
        _searchResultJson.value = "Enter keywords to search for a recipe..."
        _isLoading.value = false
    }

    fun clearMessage() {
        _message.value = null
    }

    fun generateShoppingList(recipe: RecipeDto) {

        viewModelScope.launch {
            val listName = "Recipe: ${recipe.title ?: "Unknown"}"
            val newListId = shoppingRepository.insertList(listName);

            recipe.extendedIngredients.forEach { ingredient ->
                val item = ShoppingItem(
                    listId = newListId.toInt(),
                    name = ingredient.original,
                    isChecked = false
                )
                shoppingRepository.insertItem(item)
            }
            _message.value = "Shopping list created!"
        }
    }
}