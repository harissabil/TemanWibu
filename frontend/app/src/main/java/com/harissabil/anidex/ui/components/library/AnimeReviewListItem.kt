package com.harissabil.anidex.ui.components.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import com.harissabil.anidex.ui.theme.spacing
import com.harissabil.anidex.util.localizeDate

@Composable
fun AnimeReviewListItem(
    anime: com.harissabil.anidex.data.remote.projekbasdat.dto.review.Data,
    onAnimeClicked: (String) -> Unit,
    onReviewDeleted: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.clickable { onAnimeClicked(anime.anime_id) }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(data = anime.poster_image)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .memoryCacheKey(anime.anime_id)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .diskCacheKey(anime.anime_id)
                    .allowHardware(false)
                    .allowRgb565(true)
                    .crossfade(enable = true)
                    .build(),
                contentDescription = "Cover Image from Anime ${anime.title}",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(
                    colorMatrix = ColorMatrix().apply {
                        setToSaturation(sat = 0.85F)
                    }
                ),
                filterQuality = FilterQuality.Medium,
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(5f / 7f)
                    .clip(RoundedCornerShape(MaterialTheme.spacing.small))
                    .clickable {
                        onAnimeClicked(anime.anime_id)
                    }
            )
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 8.dp
                    )
            ) {
                Row {
                    Column(
                        modifier = Modifier.weight(1f, fill = true)
                    ) {
                        Text(
                            text = anime.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        RatingBar(
                            value = anime.anime_score.toFloat(),
                            style = RatingBarStyle.Fill(
                                activeColor = MaterialTheme.colorScheme.primary,
                                inActiveColor = MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.3F
                                )
                            ),
                            onValueChange = {},
                            onRatingChanged = {},
                            stepSize = StepSize.HALF,
                            size = 16.dp,
                            spaceBetween = 2.dp,
                        )
                    }
                    IconButton(onClick = { onReviewDeleted(anime.review_id) }) {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = "Delete Review"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                ExpandableReviewText(text = "\"${anime.anime_review}\"")
            }
        }
        Text(
            text = localizeDate(anime.review_date),
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Gray,
            modifier = modifier
                .align(Alignment.End)
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        )
    }
}