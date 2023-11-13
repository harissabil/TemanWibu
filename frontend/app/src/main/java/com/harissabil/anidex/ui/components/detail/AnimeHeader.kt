package com.harissabil.anidex.ui.components.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.harissabil.anidex.ui.theme.spacing


@Composable
fun AnimeHeader(
    malId: Int,
    coverImages: String,
    title: String,
    titleJapanese: String?,
    rank: Int,
    score: Double?,
    scoredBy: Int?,
    status: String
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background.copy(
                alpha = 0.75F
            ),
            MaterialTheme.colorScheme.background
        )
    )

    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(data = coverImages)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .memoryCacheKey(malId.toString())
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .diskCacheKey(malId.toString())
                    .allowHardware(false)
                    .allowRgb565(true)
                    .build(),
                contentDescription = "Cover Images",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(
                    colorMatrix = ColorMatrix().apply {
                        setToSaturation(sat = 0.5F)
                    }
                ),
                filterQuality = FilterQuality.Medium,
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2F)
                    .clip(RoundedCornerShape(4.dp))
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(4.dp))
                    .background(gradient)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.medium)
                .padding(top = MaterialTheme.spacing.small),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(data = coverImages)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .memoryCacheKey(malId.toString())
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .diskCacheKey(malId.toString())
                    .allowHardware(false)
                    .allowRgb565(true)
                    .crossfade(enable = true)
                    .build(),
                contentDescription = "Cover Image from Anime $title",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .widthIn(min = 100.dp, max = 150.dp)
                    .aspectRatio(5f / 7f)
                    .clip(RoundedCornerShape(MaterialTheme.spacing.small))
                    .weight(1f)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .weight(2f)
                    .padding(horizontal = 8.dp)
            ) {
                if (status != "Not yet aired") {
                    AnimeRank(rank = rank.toString())
                    AnimeScore(
                        score = (score ?: 0).toString(),
                        scoredBy = (scoredBy ?: 0).toString()
                    )
                }

                AnimeTitle(title = title, titleJapanese = titleJapanese)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector =
                        when (status) {
                            "Currently Airing" -> Icons.Filled.Schedule
                            "Finished Airing" -> Icons.Filled.Check
                            else -> Icons.Filled.Close
                        },
                        contentDescription = "Anime status icon"
                    )
                    Text(
                        text = status,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}