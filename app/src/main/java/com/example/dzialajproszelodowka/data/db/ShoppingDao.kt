package com.example.dzialajproszelodowka.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dzialajproszelodowka.data.model.ShoppingItem
import com.example.dzialajproszelodowka.data.model.ShoppingList
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(shoppingList: ShoppingList): Long

    @Delete
    suspend fun deleteList(shoppingList: ShoppingList)

    @Query("SELECT * FROM shopping_lists")
    fun getAllLists(): Flow<List<ShoppingList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItem)

    @Delete
    suspend fun deleteItem(item: ShoppingItem)

    @Query("SELECT * FROM shopping_items WHERE listId = :listId")
    fun getItemsForList(listId: Int): Flow<List<ShoppingItem>>
}