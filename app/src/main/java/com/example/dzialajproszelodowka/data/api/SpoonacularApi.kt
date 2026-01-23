package com.example.dzialajproszelodowka.data.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularApi {

    // tu buduję zapytanie get do wyszukiwania przepisów
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int = 10,

        @Query("addRecipeInformation") addRecipeInformation: Boolean = true,
        @Query("fillIngredients") fillIngredients: Boolean = true

    ): ResponseBody
}