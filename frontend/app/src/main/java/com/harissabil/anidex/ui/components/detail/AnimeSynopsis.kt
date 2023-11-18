package com.harissabil.anidex.ui.components.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.harissabil.anidex.data.remote.anime.dto.Data

@Composable
fun AnimeSynopsis(
    anime: Data
) {
    Text(
        text = "Synopsis",
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold
        )
    )
    ExpandableText(text = anime.synopsis!!, modifier = Modifier.fillMaxWidth().animateContentSize(), minimizedMaxLines = 3)
}