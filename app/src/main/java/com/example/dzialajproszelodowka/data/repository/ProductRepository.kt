package com.example.dzialajproszelodowka.data.repository

import com.example.dzialajproszelodowka.data.db.ProductDao
import com.example.dzialajproszelodowka.data.model.Product
import kotlinx.coroutines.flow.Flow


open class ProductRepository(private val productDao: ProductDao) {

    open val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    /**
     * Funkcja wstawiania produktu. Wywołuje funkcję 'suspend' z DAO.
     */
    // ZMIANA NAZWY:
    suspend fun insertProduct(product: Product) {
        // ZMIANA WYWOŁANIA:
        productDao.insertProduct(product)
    }

    /**
     * Funkcja usuwania produktu. Wywołuje funkcję 'suspend' z DAO.
     */
    // ZMIANA NAZWY:
    suspend fun deleteProduct(product: Product) {
        // ZMIANA WYWOŁANIA:
        productDao.deleteProduct(product)
    }

    // Funkcja z Twojego starego DAO (może się przydać)
    fun getProductByName(name: String): Flow<Product?> {
        return productDao.getProductByName(name)
    }

    // Funkcja z Twojego starego DAO (może się przydać)
    suspend fun update(product: Product) {
        productDao.update(product)
    }
}