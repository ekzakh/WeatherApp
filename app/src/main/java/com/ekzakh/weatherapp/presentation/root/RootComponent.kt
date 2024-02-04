package com.ekzakh.weatherapp.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.ekzakh.weatherapp.presentation.details.DetailsComponent
import com.ekzakh.weatherapp.presentation.favorite.FavoriteComponent
import com.ekzakh.weatherapp.presentation.search.SearchComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Favorite(val favoriteComponent: FavoriteComponent) : Child
        data class Search(val searchComponent: SearchComponent) : Child
        data class Details(val detailsComponent: DetailsComponent) : Child
    }
}
