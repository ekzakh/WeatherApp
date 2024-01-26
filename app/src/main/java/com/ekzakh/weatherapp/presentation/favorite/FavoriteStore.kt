package com.ekzakh.weatherapp.presentation.favorite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.ekzakh.weatherapp.domain.entity.City
import com.ekzakh.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.ekzakh.weatherapp.domain.usecase.GetFavoriteCitiesUseCase
import com.ekzakh.weatherapp.presentation.favorite.FavoriteStore.Intent
import com.ekzakh.weatherapp.presentation.favorite.FavoriteStore.Label
import com.ekzakh.weatherapp.presentation.favorite.FavoriteStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavoriteStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object SearchClicked : Intent
        data object AddCityClicked : Intent
        data class CityClicked(val cityId: Int) : Intent
    }

    data class State(val cities: List<CityItem>) {
        data class CityItem(
            val city: City,
            val weatherState: WeatherState,
        )

        interface WeatherState {
            object Initial : WeatherState
            object Loading : WeatherState
            object Error : WeatherState

            data class Loaded(
                val temp: Float,
                val iconUrl: String,
            ) : WeatherState
        }
    }

    sealed interface Label {
        data object SearchClicked : Label
        data object AddCityClicked : Label
        data class CityClicked(val cityId: Int) : Label
    }
}

class FavoriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
    private val gewCurrentWeatherUseCase: GetCurrentWeatherUseCase,
) {

    fun create(): FavoriteStore =
        object :
            FavoriteStore,
            Store<Intent, State, Label> by storeFactory.create(
                name = "FavoriteStore",
                initialState = State(listOf()),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action {
        data class FavoriteCitiesLoaded(val cities: List<City>) : Action
    }

    private sealed interface Msg {
        data class FavoriteCitiesLoaded(val cities: List<City>) : Msg
        data class WeatherLoaded(
            val cityId: Int,
            val temp: Float,
            val iconUrl: String,
        ) : Msg

        data class WeatherLoadingError(
            val cityId: Int,
        ) : Msg

        data class WeatherLoading(
            val cityId: Int,
        ) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavoriteCitiesUseCase().collect { cities ->
                    dispatch(Action.FavoriteCitiesLoaded(cities))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddCityClicked -> publish(Label.AddCityClicked)
                is Intent.CityClicked -> publish(Label.CityClicked(intent.cityId))
                Intent.SearchClicked -> publish(Label.SearchClicked)
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavoriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavoriteCitiesLoaded(cities))
                    scope.launch {
                        action.cities.forEach {
                            loadWeather(it.id)
                        }
                    }
                }
            }
        }

        private fun loadWeather(citiId: Int) {
            scope.launch {
                try {
                    val weather = gewCurrentWeatherUseCase(citiId)
                    dispatch(Msg.WeatherLoaded(citiId, weather.temperature, weather.conditionUrl))
                } catch (e: Exception) {
                    dispatch(Msg.WeatherLoadingError(citiId))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavoriteCitiesLoaded -> {
                copy(
                    cities = cities.map {
                        State.CityItem(it.city, weatherState = State.WeatherState.Initial)
                    },
                )
            }

            is Msg.WeatherLoaded -> copy(
                cities = cities.map { cityItem ->
                    if (cityItem.city.id == msg.cityId) {
                        State.CityItem(
                            cityItem.city,
                            weatherState = State.WeatherState.Loaded(msg.temp, msg.iconUrl),
                        )
                    } else {
                        cityItem
                    }
                },
            )

            is Msg.WeatherLoading -> copy(
                cities = cities.map { cityItem ->
                    if (cityItem.city.id == msg.cityId) {
                        State.CityItem(
                            cityItem.city,
                            weatherState = State.WeatherState.Loading,
                        )
                    } else {
                        cityItem
                    }
                },
            )

            is Msg.WeatherLoadingError -> copy(
                cities = cities.map { cityItem ->
                    if (cityItem.city.id == msg.cityId) {
                        State.CityItem(
                            cityItem.city,
                            weatherState = State.WeatherState.Error,
                        )
                    } else {
                        cityItem
                    }
                },
            )
        }
    }
}
