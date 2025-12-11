package com.example.dzialajproszelodowka.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class ProductResponse(
    val product: ProductData?
)

data class ProductData(
    val product_name: String?
)

interface OpenFoodFactsService {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getProduct(@Path("barcode") barcode: String): ProductResponse
}

object OpenFoodFactsClient {
    private const val BASE_URL = "https://world.openfoodfacts.org/"

    val api: OpenFoodFactsService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsService::class.java)
    }
}