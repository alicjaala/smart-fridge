package com.example.dzialajproszelodowka.data.repository

import com.example.dzialajproszelodowka.data.api.SpoonacularApi

class RecipeRepository(private val api: SpoonacularApi) {

    private val API_KEY = "c05ce67f93244eadb040311916d73f3f"

    suspend fun searchRecipes(query: String): String {
        return try {
            val response = api.searchRecipes(query, API_KEY)
            response.string()
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}