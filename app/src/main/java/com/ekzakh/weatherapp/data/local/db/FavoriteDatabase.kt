package com.ekzakh.weatherapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ekzakh.weatherapp.data.local.model.CityDbModel

@Database(entities = [CityDbModel::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteCitiesDao

    companion object {
        private var INSTANCE: FavoriteDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): FavoriteDatabase {
            INSTANCE?.let { return it }
            synchronized(LOCK) {
                val database = Room.databaseBuilder(
                    context,
                    FavoriteDatabase::class.java,
                    "FavoriteDatabase",
                ).build()
                INSTANCE = database
                return database
            }
        }
    }
}
