package com.example.dzialajproszelodowka.data.repository

import com.example.dzialajproszelodowka.data.db.ProductDao
import com.example.dzialajproszelodowka.data.model.Product
import kotlinx.coroutines.flow.Flow

// podczas tworzenia repozytorium podajemu mu instancję productDao, private val automatycznie
// robi z niego pole klasy
class ProductRepository(private val productDao: ProductDao) {

    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    fun getProductByName(name: String): Flow<Product?> {
        return productDao.getProductByName(name)
    }

    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }
}