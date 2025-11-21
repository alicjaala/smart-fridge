package com.example.dzialajproszelodowka

import android.app.Application
import com.example.dzialajproszelodowka.data.db.AppDatabase
import com.example.dzialajproszelodowka.data.repository.ProductRepository
import com.example.dzialajproszelodowka.data.repository.ShoppingRepository // <-- NOWE

class FridgeApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { ProductRepository(database.productDao()) }

    // NOWE REPOZYTORIUM
    val shoppingRepository by lazy { ShoppingRepository(database.shoppingDao()) }
}