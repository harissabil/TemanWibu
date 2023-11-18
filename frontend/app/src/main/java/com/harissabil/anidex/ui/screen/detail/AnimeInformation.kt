package com.harissabil.anidex.ui.screen.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.harissabil.anidex.data.remote.anime.dto.Data


@Composable
fun AnimeInformation(
    anime: Data,
) {
    Text(
        text = "Information",
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold
        )
    )

    //  Type
    if (anime.type != null) {
        Row {
            Text(text = "Type")
            Text(
                text = anime.type,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

    //  Episodes
    if (anime.episodes != null) {
        Row {
            Text(text = "Episodes")
            Text(
                text = anime.episodes.toString(),
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

    //  Season & Year
    if (anime.season != null && anime.year != null) {
        val season = anime.season
            .replaceFirstChar {
                it.titlecase()
            }
        val seasonWithYear = "$season ${anime.year}"

        Row {
            Text(text = "Season")
            Text(
                text = seasonWithYear,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

    //  Airing Information
    if (anime.aired.string != null) {
        Row {
            Text(text = "Aired")
            Text(
                text = anime.aired.string,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

    //  Studio
    if (anime.studios.isNotEmpty()) {
        val studios = mutableListOf<String>()
        for (i in anime.studios.listIterator()) {
            studios.add(i.name)
        }
        Row {
            Text(text = "Studio")
            Text(
                text = studios.toString().removeSurrounding("[", "]"),
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f),
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

    //  Duration
    Row {
        Text(text = "Duration")
        Text(
            text = anime.duration,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)

    //  Rating
    Row {
        Text(text = "Rating")
        Text(
            text = anime.rating ?: "Unknown",
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

