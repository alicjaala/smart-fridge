package com.example.dzialajproszelodowka.data.repository

import com.example.dzialajproszelodowka.data.api.OpenFoodFactsClient

class BarcodeRepository {
    suspend fun getProductName(barcode: String): String? {
        return try {
            val response = OpenFoodFactsClient.api.getProduct(barcode)
            response.product?.product_name
        } catch (e: Exception) {
            null
        }
    }
}