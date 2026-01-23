package com.example.dzialajproszelodowka

import com.example.dzialajproszelodowka.ui.recipe.RecipeViewModel


import com.example.dzialajproszelodowka.data.repository.ProductRepository
import com.example.dzialajproszelodowka.data.repository.RecipeRepository
import com.example.dzialajproszelodowka.data.repository.ShoppingRepository
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeMainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeViewModelTest {

    @get:Rule
    val mainDispatcherRule = RecipeMainDispatcherRule()

    private val recipeRepository = mockk<RecipeRepository>(relaxed = true)
    private val shoppingRepository = mockk<ShoppingRepository>(relaxed = true)
    private val productRepository = mockk<ProductRepository>(relaxed = true)

    private lateinit var viewModel: RecipeViewModel

    @Test
    fun `test resetSearchState`() = runTest {
        viewModel = RecipeViewModel(recipeRepository, shoppingRepository, productRepository)

        viewModel.resetSearchState()

        val expectedDefault = "Enter keywords to search for a recipe..."
        assertEquals(expectedDefault, viewModel.searchResultJson.value)

        assertEquals(false, viewModel.isLoading.value)
    }
}