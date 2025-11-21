package com.example.dzialajproszelodowka.data.repository

import com.example.dzialajproszelodowka.data.db.ShoppingDao
import com.example.dzialajproszelodowka.data.model.ShoppingList
import kotlinx.coroutines.flow.Flow

class ShoppingRepository(private val shoppingDao: ShoppingDao) {

    val allLists: Flow<List<ShoppingList>> = shoppingDao.getAllLists()

    suspend fun insertList(name: String) {
        val newList = ShoppingList(name = name)
        shoppingDao.insertList(newList)
    }

    suspend fun deleteList(shoppingList: ShoppingList) {
        shoppingDao.deleteList(shoppingList)
    }
}