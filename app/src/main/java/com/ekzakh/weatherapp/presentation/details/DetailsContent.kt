package com.ekzakh.weatherapp.presentation.details

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ekzakh.weatherapp.R
import com.ekzakh.weatherapp.domain.entity.Forecast
import com.ekzakh.weatherapp.domain.entity.Weather
import com.ekzakh.weatherapp.presentation.extentions.formattedDateFull
import com.ekzakh.weatherapp.presentation.extentions.formattedDateShort
import com.ekzakh.weatherapp.presentation.extentions.tempToFormatted
import com.ekzakh.weatherapp.presentation.ui.theme.CardGradients

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsContent(detailsComponent: DetailsComponent) {
    val model by detailsComponent.model.collectAsState()
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                cityName = model.city.name,
                isFavorite = model.isFavorite,
                onBackClick = { detailsComponent.onClickBack() },
                changeFavoriteCLick = { detailsComponent.changeFavorite() },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CardGradients.gradients[1].primaryGradient),
        ) {
            when (val state = model.forecastState) {
                DetailsStore.State.ForecastState.Error -> {
                }

                DetailsStore.State.ForecastState.Initial -> {
                }

                DetailsStore.State.ForecastState.Loading -> {
                    Loading()
                }

                is DetailsStore.State.ForecastState.Success -> {
                    Forecast(forecast = state.forecast)
                }
            }
        }
    }
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun Forecast(forecast: Forecast) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1F))
        Text(
            text = forecast.currentWeather.conditionText,
            style = MaterialTheme.typography.titleLarge,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = forecast.currentWeather.temperature.tempToFormatted(),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 48.sp),
            )
            GlideImage(
                modifier = Modifier.size(70.dp),
                model = forecast.currentWeather.conditionUrl,
                contentDescription = null,
            )
        }
        Text(
            text = forecast.currentWeather.date.formattedDateFull(),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.weight(1F))
        AnimatedUpcoming(forecast.upcoming)
        Spacer(modifier = Modifier.weight(0.5F))
    }
}

@Composable
private fun AnimatedUpcoming(upcoming: List<Weather>) {
    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(animationSpec = tween(500)) + slideIn(
            animationSpec = tween(500),
            initialOffset = { size -> IntOffset(0, size.height) },
        ),
    ) {
        Upcoming(upcoming = upcoming)
    }
}

@Composable
private fun Upcoming(upcoming: List<Weather>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(
                alpha = 0.2F,
            ),
        ),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp),
            text = stringResource(R.string.upcoming),
            style = MaterialTheme.typography.headlineLarge,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            upcoming.forEach {
                SmallWeatherCard(weather = it)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RowScope.SmallWeatherCard(weather: Weather) {
    Card(
        modifier = Modifier
            .height(150.dp)
            .padding(16.dp)
            .weight(1F),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = weather.temperature.tempToFormatted(),
                style = MaterialTheme.typography.bodyLarge,
            )
            GlideImage(
                modifier = Modifier.size(48.dp),
                model = weather.conditionUrl,
                contentDescription = null,
            )
            Text(
                text = weather.date.formattedDateShort(),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    cityName: String,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    changeFavoriteCLick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(
                text = cityName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.background,
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background,
                )
            }
        },
        actions = {
            val favoriteIcon = if (isFavorite) {
                Icons.Default.Star
            } else {
                Icons.Default.StarBorder
            }
            IconButton(onClick = { changeFavoriteCLick() }) {
                Icon(
                    imageVector = favoriteIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background,
                )
            }
        },
    )
}
