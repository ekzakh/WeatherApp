package com.ekzakh.weatherapp.di

import android.content.Context
import com.ekzakh.weatherapp.data.local.db.FavoriteCitiesDao
import com.ekzakh.weatherapp.data.local.db.FavoriteDatabase
import com.ekzakh.weatherapp.data.network.api.ApiFactory
import com.ekzakh.weatherapp.data.network.api.ApiService
import com.ekzakh.weatherapp.data.repository.FavoriteRepositoryImpl
import com.ekzakh.weatherapp.data.repository.SearchRepositoryImpl
import com.ekzakh.weatherapp.data.repository.WeatherRepositoryImpl
import com.ekzakh.weatherapp.domain.repository.FavoriteRepository
import com.ekzakh.weatherapp.domain.repository.SearchRepository
import com.ekzakh.weatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @[ApplicationScope Binds]
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @[ApplicationScope Binds]
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    companion object {

        @[ApplicationScope Provides]
        fun provideFavoriteDatabase(context: Context): FavoriteDatabase =
            FavoriteDatabase.getInstance(context)

        @[ApplicationScope Provides]
        fun provideFavoriteDao(database: FavoriteDatabase): FavoriteCitiesDao =
            database.favoriteDao()

        @[ApplicationScope Provides]
        fun provideApiService(): ApiService = ApiFactory.create(ApiService::class.java)
    }
}
