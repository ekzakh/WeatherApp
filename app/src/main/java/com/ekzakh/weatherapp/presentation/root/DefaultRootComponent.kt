package com.ekzakh.weatherapp.presentation.root

import com.arkivanov.decompose.ComponentContext

class DefaultRootComponent(private val componentContext: ComponentContext) : RootComponent, ComponentContext by componentContext
