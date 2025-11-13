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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    // ZMIANA NAZWY:
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun update(product: Product) // Tę zostawmy, bo jest rzadko używana

    @Delete
    // ZMIANA NAZWY:
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM products_table ORDER BY expiryDate ASC")
    fun getAllProducts(): Flow<List<Product>>

    // Dodajmy też 'getProductByName', bo jest w Twoim starym DAO (dla kompletności)
    @Query("SELECT * FROM products_table WHERE name = :productName")
    fun getProductByName(productName: String): Flow<Product?>
}