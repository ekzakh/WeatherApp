package com.ekzakh.weatherapp.presentation.search

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

class DefaultSearchComponent @AssistedInject constructor(
    private val searchStoreFactory: SearchStoreFactory,
    @Assisted private val openReason: OpenReason,
    @Assisted private val componentContext: ComponentContext,
    @Assisted("onClickBack") private val onClickBack: () -> Unit,
    @Assisted private val onCityClick: (Int) -> Unit,
    @Assisted("onSavedToFavorite") private val onSavedToFavorite: () -> Unit,
) : SearchComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private val store = instanceKeeper.getStore { searchStoreFactory.create(openReason) }

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    SearchStore.Label.ClickBack -> {
                        onClickBack.invoke()
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

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted openReason: OpenReason,
            @Assisted componentContext: ComponentContext,
            @Assisted("onClickBack") onClickBack: () -> Unit,
            @Assisted onCityClick: (Int) -> Unit,
            @Assisted("onSavedToFavorite") onSavedToFavorite: () -> Unit,
        ): DefaultSearchComponent
    }
}
