package com.example.dzialajproszelodowka.data.repository

import com.example.dzialajproszelodowka.data.db.ShoppingDao
import com.example.dzialajproszelodowka.data.model.ShoppingItem
import com.example.dzialajproszelodowka.data.model.ShoppingList
import kotlinx.coroutines.flow.Flow

class ShoppingRepository(private val shoppingDao: ShoppingDao) {

    val allLists: Flow<List<ShoppingList>> = shoppingDao.getAllLists()

    suspend fun insertList(name: String): Long {
        val newList = ShoppingList(name = name)
        return shoppingDao.insertList(newList)
    }

    suspend fun deleteList(shoppingList: ShoppingList) {
        shoppingDao.deleteList(shoppingList)
    }


    fun getItemsForList(listId: Int): Flow<List<ShoppingItem>> {
        return shoppingDao.getItemsForList(listId)
    }

    suspend fun insertItem(item: ShoppingItem) {
        shoppingDao.insertItem(item)
    }

    suspend fun deleteItem(item: ShoppingItem) {
        shoppingDao.deleteItem(item)
    }

    suspend fun updateItem(item: ShoppingItem) {
        shoppingDao.insertItem(item)
    }
}