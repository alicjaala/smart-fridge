package com.example.dzialajproszelodowka.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val listId: Int,
    val name: String,
    val isChecked: Boolean = false
)