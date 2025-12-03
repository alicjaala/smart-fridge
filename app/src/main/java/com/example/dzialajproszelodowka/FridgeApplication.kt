package com.example.dzialajproszelodowka

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dzialajproszelodowka.data.db.AppDatabase
import com.example.dzialajproszelodowka.data.repository.ProductRepository
import com.example.dzialajproszelodowka.data.repository.ShoppingRepository
import com.example.dzialajproszelodowka.worker.ExpiryWorker
import java.util.concurrent.TimeUnit
import com.example.dzialajproszelodowka.data.api.RetrofitClient
import com.example.dzialajproszelodowka.data.repository.RecipeRepository


class FridgeApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { ProductRepository(database.productDao()) }
    val shoppingRepository by lazy { ShoppingRepository(database.shoppingDao()) }
    val recipeRepository by lazy { RecipeRepository(RetrofitClient.api) }


    override fun onCreate() {
        super.onCreate()
        setupRecurringWork()
    }




    private fun setupRecurringWork() {

        val workRequest = PeriodicWorkRequestBuilder<ExpiryWorker>(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ExpiryCheckWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }


}