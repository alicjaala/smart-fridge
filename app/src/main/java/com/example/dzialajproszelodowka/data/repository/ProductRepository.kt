package com.example.dzialajproszelodowka.data.repository

import com.example.dzialajproszelodowka.data.db.ProductDao
import com.example.dzialajproszelodowka.data.model.Product
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Date


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

    suspend fun getProductsExpiringTomorrow(): List<Product> {
        val calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, 1)

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startOfTomorrow = calendar.time

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endOfTomorrow = calendar.time

        return productDao.getProductsBetweenDates(startOfTomorrow, endOfTomorrow)
    }
}