package com.example.dzialajproszelodowka

import android.app.Application
import com.example.dzialajproszelodowka.data.db.AppDatabase
import com.example.dzialajproszelodowka.data.repository.ProductRepository

class FridgeApplication: Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { ProductRepository(database.productDao()) }
}