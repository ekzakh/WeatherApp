package com.ekzakh.weatherapp.presentation.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import com.ekzakh.weatherapp.presentation.ui.theme.CardGradients
import com.ekzakh.weatherapp.presentation.ui.theme.Gradient

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
        itemsIndexed(items = state.cities, key = { _, item -> item.city.id }) { index, item ->
            CityCard(index, item)
        }
    }
}

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
            Text(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = item.city.name,
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

private fun getGradientByIndex(index: Int): Gradient {
    val gradients = CardGradients.gradients
    return gradients[index % gradients.size]
}
