package com.ekzakh.weatherapp.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.ekzakh.weatherapp.domain.entity.City
import com.ekzakh.weatherapp.domain.entity.Forecast
import com.ekzakh.weatherapp.domain.usecase.AddToFavoriteUseCase
import com.ekzakh.weatherapp.domain.usecase.GetForecastUseCase
import com.ekzakh.weatherapp.domain.usecase.ObserveFavoriteCityUseCase
import com.ekzakh.weatherapp.domain.usecase.RemoveFromFavoriteUseCase
import com.ekzakh.weatherapp.presentation.details.DetailsStore.Intent
import com.ekzakh.weatherapp.presentation.details.DetailsStore.Label
import com.ekzakh.weatherapp.presentation.details.DetailsStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ChangeFavorite : Intent
        data object ClickBack : Intent
    }

    data class State(
        val city: City,
        val isFavorite: Boolean,
        val forecastState: ForecastState,
    ) {
        sealed interface ForecastState {
            data object Initial : ForecastState
            data object Loading : ForecastState
            data class Success(val forecast: Forecast) : ForecastState
            data object Error : ForecastState
        }
    }

    sealed interface Label {
        data object ClickBack : Label
    }
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val observeFavoriteUseCase: ObserveFavoriteCityUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val removeFromFavoriteUseCase: RemoveFromFavoriteUseCase,
) {

    fun create(city: City): DetailsStore =
        object :
            DetailsStore,
            Store<Intent, State, Label> by storeFactory.create(
                name = "DetailsStore",
                initialState = State(
                    city = city,
                    isFavorite = false,
                    forecastState = State.ForecastState.Initial,
                ),
                bootstrapper = BootstrapperImpl(city.id),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action {
        data object Loading : Action
        data class Success(val forecast: Forecast) : Action
        data object Error : Action
        data class ChangeFavorite(val isFavorite: Boolean) : Action
    }

    private sealed interface Msg {
        data object Loading : Msg
        data class Success(val forecast: Forecast) : Msg
        data object Error : Msg
        data class ChangeFavorite(val isFavorite: Boolean) : Msg
    }

    private inner class BootstrapperImpl(private val cityId: Int) :
        CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                observeFavoriteUseCase.invoke(cityId)
                    .collect { dispatch(Action.ChangeFavorite(it)) }
            }
            scope.launch {
                try {
                    dispatch(Action.Loading)
                    val forecast = getForecastUseCase.invoke(cityId)
                    dispatch(Action.Success(forecast))
                } catch (e: Exception) {
                    dispatch(Action.Error)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ChangeFavorite -> {
                    val state = getState()
                    scope.launch {
                        if (state.isFavorite) {
                            removeFromFavoriteUseCase.invoke(state.city.id)
                        } else {
                            addToFavoriteUseCase.invoke(state.city)
                        }
                    }
                }

                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.ChangeFavorite -> {
                    dispatch(Msg.ChangeFavorite(action.isFavorite))
                }

                Action.Error -> {
                    dispatch(Msg.Error)
                }

                Action.Loading -> {
                    dispatch(Msg.Loading)
                }

                is Action.Success -> {
                    dispatch(Msg.Success(action.forecast))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeFavorite -> {
                copy(isFavorite = msg.isFavorite)
            }

            Msg.Error -> {
                copy(forecastState = State.ForecastState.Error)
            }

            Msg.Loading -> {
                copy(forecastState = State.ForecastState.Loading)
            }

            is Msg.Success -> {
                copy(forecastState = State.ForecastState.Success(msg.forecast))
            }
        }
    }
}
