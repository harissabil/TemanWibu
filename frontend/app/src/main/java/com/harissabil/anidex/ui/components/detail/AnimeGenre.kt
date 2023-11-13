package com.harissabil.anidex.ui.components.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.harissabil.anidex.data.remote.anime.dto.Data
import com.harissabil.anidex.ui.theme.spacing

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnimeGenre(
    anime: Data
) {
    Text(
        text = "Genre",
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold
        )
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        maxItemsInEachRow = 5
    ) {
        if (anime.genres.isNotEmpty()) {
            anime.genres.forEach { genre ->
                AssistChip(
                    onClick = { /*TODO*/ },
                    label = { Text(text = genre.name) }
                )
            }
        }
    }
}