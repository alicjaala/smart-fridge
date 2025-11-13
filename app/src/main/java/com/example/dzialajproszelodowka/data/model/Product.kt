package com.example.dzialajproszelodowka.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "products_table")
data class Product(
    @PrimaryKey
    val name: String,
    val amount: Int,
    val expiryDate: Date
)
