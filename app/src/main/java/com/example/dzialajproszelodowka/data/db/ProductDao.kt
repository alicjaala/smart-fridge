package com.example.dzialajproszelodowka.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dzialajproszelodowka.data.model.Product
import kotlinx.coroutines.flow.Flow
import java.util.Date

// insert, update i delete nie trzeba implementować
@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM products_table ORDER BY expiryDate ASC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products_table WHERE name = :productName")
    fun getProductByName(productName: String): Flow<Product?>

    @Query("SELECT * FROM products_table WHERE expiryDate BETWEEN :from AND :to")
    suspend fun getProductsBetweenDates(from: Date, to: Date): List<Product>
}