package com.example.dzialajproszelodowka.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dzialajproszelodowka.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    // suspend - boczny wątek
    // jeśli wstawimy produkt który już istnieje, stary produkt zostanie zastąpiony
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)


    // flow - jako odpowiedź dostaniemy strumień danych, będzie wysyłane za każdym razem
    // kiedy coś w produkcie się zmieni
    @Query("SELECT * FROM products_table ORDER BY expiryDate ASC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products_table WHERE name = :productName")
    fun getProductByName(productName: String): Flow<Product?>
}