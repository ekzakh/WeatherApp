package com.ekzakh.weatherapp.presentation.favorite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.ekzakh.weatherapp.domain.entity.City
import com.ekzakh.weatherapp.presentation.extentions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultFavoriteComponent @AssistedInject constructor(
    private val favoriteStoreFactory: FavoriteStoreFactory,
    @Assisted private val componentContext: ComponentContext,
    @Assisted("onAddCityClick") private val onAddCityClick: () -> Unit,
    @Assisted("onSearchClick") private val onSearchClick: () -> Unit,
    @Assisted private val onCityClick: (City) -> Unit,
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
                        onCityClick(label.city)
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
        store.accept(FavoriteStore.Intent.CityClicked(city))

    override fun addToFavoriteClick() =
        store.accept(FavoriteStore.Intent.AddCityClicked)

    override fun searchClick() =
        store.accept(FavoriteStore.Intent.SearchClicked)

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted componentContext: ComponentContext,
            @Assisted("onAddCityClick") onAddCityClick: () -> Unit,
            @Assisted("onSearchClick") onSearchClick: () -> Unit,
            @Assisted onCityClick: (City) -> Unit,
        ): DefaultFavoriteComponent
    }
}
