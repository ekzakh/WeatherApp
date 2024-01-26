package com.ekzakh.weatherapp

import android.app.Application
import com.ekzakh.weatherapp.di.AppComponent
import com.ekzakh.weatherapp.di.DaggerAppComponent

class WeatherApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}
