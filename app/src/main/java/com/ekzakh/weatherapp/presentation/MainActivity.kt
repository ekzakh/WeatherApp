package com.ekzakh.weatherapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.ekzakh.weatherapp.WeatherApp
import com.ekzakh.weatherapp.presentation.root.DefaultRootComponent
import com.ekzakh.weatherapp.presentation.root.RootContent
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as WeatherApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        val rootComponent = rootComponentFactory.create(defaultComponentContext())
        setContent {
            RootContent(rootComponent = rootComponent)
        }
    }
}
