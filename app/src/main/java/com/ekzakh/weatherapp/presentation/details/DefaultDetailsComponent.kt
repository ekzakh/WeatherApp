package com.ekzakh.weatherapp.presentation.details

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

class DefaultDetailsComponent @AssistedInject constructor(
    private val detailsStoreFactory: DetailsStoreFactory,
    @Assisted private val city: City,
    @Assisted private val componentContext: ComponentContext,
    @Assisted private val onClickBack: () -> Unit,
) : DetailsComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private val store = instanceKeeper.getStore { detailsStoreFactory.create(city) }

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    DetailsStore.Label.ClickBack -> onClickBack.invoke()
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

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted city: City,
            @Assisted componentContext: ComponentContext,
            @Assisted onClickBack: () -> Unit,
        ): DefaultDetailsComponent
    }
}
