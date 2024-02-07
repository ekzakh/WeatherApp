package com.ekzakh.weatherapp.presentation.favorite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ekzakh.weatherapp.R
import com.ekzakh.weatherapp.presentation.extentions.tempToFormatted
import com.ekzakh.weatherapp.presentation.ui.theme.CardGradients
import com.ekzakh.weatherapp.presentation.ui.theme.Gradient
import com.ekzakh.weatherapp.presentation.ui.theme.Orange

@Composable
fun FavoriteContent(favoriteComponent: FavoriteComponent) {
    val state by favoriteComponent.model.collectAsState()
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item(span = { GridItemSpan(2) }) {
            SearchCard(onClick = { favoriteComponent.searchClick() })
        }
        itemsIndexed(items = state.cities, key = { _, item -> item.city.id }) { index, item ->
            CityCard(index, item)
        }
        item {
            AddToFavorite(onClick = { favoriteComponent.addToFavoriteClick() })
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun CityCard(index: Int, item: FavoriteStore.State.CityItem) {
    val gradient = getGradientByIndex(index)
    Card(
        modifier = Modifier
            .fillMaxSize()
            .shadow(
                elevation = 16.dp,
                shape = MaterialTheme.shapes.extraLarge,
                spotColor = gradient.shadowColor,
            ),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient.primaryGradient)
                .sizeIn(minHeight = 196.dp)
                .drawBehind {
                    drawCircle(
                        brush = gradient.secondaryGradient,
                        center = Offset(
                            x = center.x - size.width / 10,
                            y = center.y + size.height / 2,
                        ),
                    )
                }
                .padding(16.dp),
        ) {
            when (val state = item.weatherState) {
                FavoriteStore.State.WeatherState.Error -> {
                }

                FavoriteStore.State.WeatherState.Initial -> {
                }

                is FavoriteStore.State.WeatherState.Loaded -> {
                    GlideImage(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(56.dp),
                        model = state.iconUrl,
                        contentDescription = null,
                    )
                    Text(
                        modifier = Modifier.align(Alignment.BottomStart)
                            .padding(bottom = 24.dp),
                        text = state.temp.tempToFormatted(),
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 48.sp),
                    )
                }

                FavoriteStore.State.WeatherState.Loading -> {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
                }
            }
            Text(
                modifier = Modifier.align(Alignment.BottomStart),
                text = item.city.name,
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun SearchCard(onClick: () -> Unit) {
    Card(shape = MaterialTheme.shapes.extraLarge) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(CardGradients.gradients[3].primaryGradient)
                .padding(16.dp)
                .clickable { onClick() },
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.background,
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.background,
                text = stringResource(R.string.search),
            )
        }
    }
}

@Composable
private fun AddToFavorite(onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = MaterialTheme.shapes.extraLarge,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .clickable { onClick() }
                .sizeIn(minHeight = 196.dp)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(46.dp),
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = Orange,
            )
            Spacer(modifier = Modifier.weight(1F))
            Text(
                text = stringResource(R.string.add_city),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

private fun getGradientByIndex(index: Int): Gradient {
    val gradients = CardGradients.gradients
    return gradients[index % gradients.size]
}
