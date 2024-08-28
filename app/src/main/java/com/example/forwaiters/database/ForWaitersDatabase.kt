package com.example.forwaiters.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [Piano::class, Tavolo::class, Categoria::class, Piatto::class, TavoloPiatto::class], version = 1,exportSchema = false)
abstract class ForWaitersDatabase : RoomDatabase() {
    abstract val pianoDao: PianoDao
    abstract val tavoloDao: TavoloDao
    abstract val categoriaDao: CategoriaDao
    abstract val piattoDao: PiattoDao
    abstract val tavoloPiattoDao: TavoloPiattoDao
    companion object {
        @Volatile
        private var INSTANCE: ForWaitersDatabase? = null
        fun getInstance(context: Context): ForWaitersDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ForWaitersDatabase::class.java,
                    "podkova_database" // Nome del file del database
                )
                    .addCallback(DatabaseCallback(context.applicationContext))
                    .build()
                INSTANCE = instance
                // Ritorna l'istanza del database
                instance
            }
        }
    }
}

