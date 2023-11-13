package com.harissabil.anidex.ui.components.detail

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.harissabil.anidex.R

@Composable
fun AnimeRank(rank: String) {
    Text(
        text = stringResource(id = R.string.rank, rank),
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Black
        )
    )
}