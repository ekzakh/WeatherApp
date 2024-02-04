package com.ekzakh.weatherapp.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.ekzakh.weatherapp.domain.entity.City
import com.ekzakh.weatherapp.presentation.details.DefaultDetailsComponent
import com.ekzakh.weatherapp.presentation.favorite.DefaultFavoriteComponent
import com.ekzakh.weatherapp.presentation.search.DefaultSearchComponent
import com.ekzakh.weatherapp.presentation.search.OpenReason
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class DefaultRootComponent @AssistedInject constructor(
    private val favoriteComponentFactory: DefaultFavoriteComponent.Factory,
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val searchComponentFactory: DefaultSearchComponent.Factory,
    @Assisted private val componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Favorite,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child {
        return when (config) {
            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    componentContext = componentContext,
                    city = config.city,
                    onClickBack = { navigation.pop() },
                )
                RootComponent.Child.Details(component)
            }

            Config.Favorite -> {
                val component = favoriteComponentFactory.create(
                    componentContext = componentContext,
                    onAddCityClick = { navigation.push(Config.Search(OpenReason.AddToFavorite)) },
                    onSearchClick = { navigation.push(Config.Search(OpenReason.RegularSearch)) },
                    onCityClick = { navigation.push(Config.Details(it)) },
                )
                RootComponent.Child.Favorite(component)
            }

            is Config.Search -> {
                val component = searchComponentFactory.create(
                    openReason = config.openReason,
                    componentContext = componentContext,
                    onClickBack = { navigation.pop() },
                    onCityClick = { navigation.push(Config.Details(it)) },
                    onSavedToFavorite = { navigation.pop() },
                )
                RootComponent.Child.Search(component)
            }
        }
    }

    sealed interface Config : Parcelable {

        @Parcelize
        data object Favorite : Config

        @Parcelize
        data class Search(val openReason: OpenReason) : Config

        @Parcelize
        data class Details(val city: City) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(componentContext: ComponentContext): DefaultRootComponent
    }
}
