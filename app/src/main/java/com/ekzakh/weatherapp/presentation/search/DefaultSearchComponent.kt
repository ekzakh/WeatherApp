package com.ekzakh.weatherapp.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.ekzakh.weatherapp.domain.entity.City
import com.ekzakh.weatherapp.presentation.extentions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultSearchComponent(
    private val searchStoreFactory: SearchStoreFactory,
    private val openReason: OpenReason,
    private val componentContext: ComponentContext,
    private val onClickBack: () -> Unit,
    private val onCityClick: (Int) -> Unit,
    private val onSavedToFavorite: () -> Unit,
) : SearchComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private val store = instanceKeeper.getStore { searchStoreFactory.create(openReason) }

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    SearchStore.Label.ClickBack -> {
                        onClickBack()
                    }

                    is SearchStore.Label.CityClick -> {
                        onCityClick(label.city.id)
                    }

                    SearchStore.Label.SavedToFavorite -> {
                        onSavedToFavorite()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State> = store.stateFlow

    override fun changeSearchQuery(query: String) =
        store.accept(SearchStore.Intent.ChangeSearchQuery(query))

    override fun onSearchClick() = store.accept(SearchStore.Intent.ClickSearch)

    override fun onClickBack() = store.accept(SearchStore.Intent.ClickBack)

    override fun onCityClick(city: City) = store.accept(SearchStore.Intent.ClickCity(city))
}
