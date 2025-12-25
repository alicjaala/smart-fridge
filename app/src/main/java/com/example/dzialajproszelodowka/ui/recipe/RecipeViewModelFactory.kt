package com.example.dzialajproszelodowka.ui.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dzialajproszelodowka.data.repository.ProductRepository
import com.example.dzialajproszelodowka.data.repository.RecipeRepository
import com.example.dzialajproszelodowka.data.repository.ShoppingRepository

class RecipeViewModelFactory(
    private val recipeRepository: RecipeRepository,
    private val shoppingRepository: ShoppingRepository,
    private val produktRepository: ProductRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(recipeRepository, shoppingRepository, produktRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}