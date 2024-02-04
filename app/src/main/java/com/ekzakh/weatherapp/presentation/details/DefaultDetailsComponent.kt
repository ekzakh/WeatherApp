package com.ekzakh.weatherapp.presentation.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.ekzakh.weatherapp.domain.entity.City
import com.ekzakh.weatherapp.presentation.extentions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultDetailsComponent @Inject constructor(
    private val city: City,
    private val detailsStoreFactory: DetailsStoreFactory,
    private val componentContext: ComponentContext,
    private val onClickBack: () -> Unit,
) :
    DetailsComponent,
    ComponentContext by componentContext {

    private val scope = componentScope()
    private val store = instanceKeeper.getStore { detailsStoreFactory.create(city) }

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    DetailsStore.Label.ClickBack -> onClickBack()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State> = store.stateFlow

    override fun onClickBack() {
        store.accept(DetailsStore.Intent.ClickBack)
    }

    override fun changeFavorite() {
        store.accept(DetailsStore.Intent.ChangeFavorite)
    }
}
