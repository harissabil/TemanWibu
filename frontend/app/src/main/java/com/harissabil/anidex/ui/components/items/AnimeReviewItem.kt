package com.harissabil.anidex.ui.components.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import com.harissabil.anidex.data.remote.projekbasdat.dto.review.AllReviewData
import com.harissabil.anidex.ui.components.library.ExpandableReviewText
import com.harissabil.anidex.util.localizeDate

@Composable
fun AnimeReviewItem(
    anime: AllReviewData,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column {
                Row {
                    Column(
                        modifier = Modifier
                            .weight(1f, fill = true)
                            .align(Alignment.CenterVertically)
                    ) {
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
                    Text(
                        text = localizeDate(anime.review_date),
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                ExpandableReviewText(text = "\"${anime.anime_review}\"")
            }
        }
        Text(
            text = anime.username,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = modifier
                .align(Alignment.End)
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        )
    }
}