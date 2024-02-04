package com.ekzakh.weatherapp.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.ekzakh.weatherapp.presentation.details.DetailsContent
import com.ekzakh.weatherapp.presentation.favorite.FavoriteContent
import com.ekzakh.weatherapp.presentation.search.SearchContent
import com.ekzakh.weatherapp.presentation.ui.theme.WeatherAppTheme

@Composable
fun RootContent(rootComponent: RootComponent) {
    WeatherAppTheme {
        Children(stack = rootComponent.stack) {
            when (val instance = it.instance) {
                is RootComponent.Child.Details -> {
                    DetailsContent(detailsComponent = instance.detailsComponent)
                }
                is RootComponent.Child.Favorite -> {
                    FavoriteContent(favoriteComponent = instance.favoriteComponent)
                }
                is RootComponent.Child.Search -> {
                    SearchContent(searchComponent = instance.searchComponent)
                }
            }
        }
    }
}
