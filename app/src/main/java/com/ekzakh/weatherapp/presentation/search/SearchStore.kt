package com.ekzakh.weatherapp.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.ekzakh.weatherapp.domain.entity.City
import com.ekzakh.weatherapp.domain.usecase.AddToFavoriteUseCase
import com.ekzakh.weatherapp.domain.usecase.SearchCityUseCase
import com.ekzakh.weatherapp.presentation.search.SearchStore.Intent
import com.ekzakh.weatherapp.presentation.search.SearchStore.Label
import com.ekzakh.weatherapp.presentation.search.SearchStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeSearchQuery(val query: String) : Intent
        data object ClickBack : Intent
        data object ClickSearch : Intent
        data class ClickCity(val city: City) : Intent
    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState,
    ) {
        sealed interface SearchState {
            data object Initial : SearchState
            data object Loading : SearchState
            data class Success(val cities: List<City>) : SearchState
            data object Empty : SearchState
            data object Error : SearchState
        }
    }

    sealed interface Label {
        data object ClickBack : Label
        data object SavedToFavorite : Label
        data class CityClick(val city: City) : Label
    }
}

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchCityUseCase: SearchCityUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
) {

    fun create(openReason: OpenReason): SearchStore =
        object :
            SearchStore,
            Store<Intent, State, Label> by storeFactory.create(
                name = "SearchStore",
                initialState = State(
                    searchQuery = "",
                    searchState = State.SearchState.Initial,
                ),
                bootstrapper = BootstrapperImpl(),
                executorFactory = { ExecutorImpl(openReason) },
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action

    private sealed interface Msg {
        data object SearchLoading : Msg
        data object SearchError : Msg
        data class SearchSuccess(val cities: List<City>) : Msg
        data class ChangeSearchQuery(val query: String) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(private val openReason: OpenReason) :
        CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var job: Job? = null

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeSearchQuery -> {
                    dispatch(Msg.ChangeSearchQuery(intent.query))
                }

                is Intent.ClickCity -> {
                    scope.launch {
                        when (openReason) {
                            OpenReason.RegularSearch -> {
                                publish(Label.CityClick(intent.city))
                            }

                            OpenReason.AddToFavorite -> {
                                addToFavoriteUseCase.invoke(intent.city)
                                publish(Label.SavedToFavorite)
                            }
                        }
                    }
                }

                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                Intent.ClickSearch -> {
                    job?.cancel()
                    job = scope.launch {
                        try {
                            val cities = searchCityUseCase.invoke(getState().searchQuery)
                            dispatch(Msg.SearchSuccess(cities))
                        } catch (e: Exception) {
                            dispatch(Msg.SearchError)
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeSearchQuery -> {
                copy(searchQuery = msg.query)
            }

            Msg.SearchError -> {
                copy(searchState = State.SearchState.Error)
            }

            Msg.SearchLoading -> {
                copy(searchState = State.SearchState.Loading)
            }

            is Msg.SearchSuccess -> {
                val state = if (msg.cities.isEmpty()) {
                    State.SearchState.Empty
                } else {
                    State.SearchState.Success(msg.cities)
                }
                copy(searchState = state)
            }
        }
    }
}
