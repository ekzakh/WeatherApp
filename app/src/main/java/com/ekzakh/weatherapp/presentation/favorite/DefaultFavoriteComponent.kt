package com.ekzakh.weatherapp.presentation.favorite

import com.arkivanov.decompose.ComponentContext

class DefaultFavoriteComponent(private val componentContext: ComponentContext) :
    FavoriteComponent,
    ComponentContext by componentContext
