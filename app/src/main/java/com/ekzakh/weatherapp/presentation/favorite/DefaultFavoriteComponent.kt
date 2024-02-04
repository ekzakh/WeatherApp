package com.ekzakh.weatherapp.presentation.favorite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.ekzakh.weatherapp.domain.entity.City
import com.ekzakh.weatherapp.presentation.extentions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultFavoriteComponent(
    private val favoriteStoreFactory: FavoriteStoreFactory,
    private val componentContext: ComponentContext,
    private val onAddCityClick: () -> Unit,
    private val onSearchClick: () -> Unit,
    private val onCityClick: (Int) -> Unit,
) : FavoriteComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { favoriteStoreFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    FavoriteStore.Label.AddCityClicked -> {
                        onAddCityClick()
                    }

                    is FavoriteStore.Label.CityClicked -> {
                        onCityClick(label.cityId)
                    }

                    FavoriteStore.Label.SearchClicked -> {
                        onSearchClick()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavoriteStore.State> = store.stateFlow

    override fun clickCityItem(city: City) =
        store.accept(FavoriteStore.Intent.CityClicked(city.id))

    override fun addToFavoriteClick() =
        store.accept(FavoriteStore.Intent.AddCityClicked)

    override fun searchClick() =
        store.accept(FavoriteStore.Intent.SearchClicked)
}
