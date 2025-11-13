package com.example.dzialajproszelodowka.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dzialajproszelodowka.data.model.Product


@Database(
    entities = [Product::class],    // lista wszystkich encji
    version = 1,                    // wersja bazy (idk
    exportSchema = false            // żeby jakiegoś błędu nie było
)

@TypeConverters(Converters::class)

abstract class AppDatabase: RoomDatabase() {

    // room automatycznie generuje tu implementację
    abstract fun productDao(): ProductDao;


    // w companion object trzymamy zmienne statyczne
    companion object {

        // volatile - zmienna jest zawsze widoczna w swojej najnowszej wersji
        // singleton - przechowujemy jedną instancję bazy danych
        @Volatile
        private var INSTANCE: AppDatabase? = null;

        fun getDatabase(context: Context): AppDatabase {

            val tempInstance = INSTANCE;
            if (tempInstance != null) return tempInstance;

            // zapewnie, że nie będzie próby utworzenia bazy w kilku miejscach w tym samym czasie
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,         // baza będzie powiązana z całą aplikacją, a nie z ekranem
                    AppDatabase::class.java,
                    "smartfridge_database"
                ).build()
                INSTANCE = instance

                return instance
            }


        }
    }
}



