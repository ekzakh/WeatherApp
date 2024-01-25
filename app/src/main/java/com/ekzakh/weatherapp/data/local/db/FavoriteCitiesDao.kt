package com.ekzakh.weatherapp.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ekzakh.weatherapp.data.local.model.CityDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCitiesDao {

    @Query("SELECT * FROM favorite_cities")
    fun favoriteCities(): Flow<List<CityDbModel>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_cities WHERE id=:cityId LIMIT 1)")
    fun observeIsFavorite(cityId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(city: CityDbModel)

    @Query("DELETE FROM favorite_cities WHERE id=:cityId")
    suspend fun removeFromFavorite(cityId: Int)
}
