package com.harissabil.anidex.ui.components.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.harissabil.anidex.data.remote.anime.dto.Data
import com.harissabil.anidex.ui.theme.AniDexTheme
import com.harissabil.anidex.ui.theme.spacing

@Composable
fun AnimeItem(
    modifier: Modifier = Modifier,
    result: Data,
    onClick: (String) -> Unit
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(data = result.images.jpg.image_url)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCacheKey(result.mal_id.toString())
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCacheKey(result.mal_id.toString())
            .allowHardware(false)
            .allowRgb565(true)
            .crossfade(enable = true)
            .build(),
        contentScale = ContentScale.Crop,
        colorFilter = ColorFilter.colorMatrix(
            colorMatrix = ColorMatrix().apply {
                setToSaturation(sat = 0.85F)
            }
        ),
        filterQuality = FilterQuality.Low,
        contentDescription = result.title,
        modifier = Modifier
            .width(200.dp)
            .aspectRatio(5f / 7f)
            .clip(RoundedCornerShape(MaterialTheme.spacing.small))
            .clickable {
                onClick(result.mal_id.toString())
            }
            .onGloballyPositioned {
                sizeImage = it.size
            }
            .then(modifier),
    )
}

@Preview
@Composable
fun AnimeItemPrev() {
    AniDexTheme {
    }
}

//"https://image.tmdb.org/t/p/w500${result.poster_path}"