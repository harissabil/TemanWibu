package com.harissabil.anidex.ui.components.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun LibraryTabs(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    tabs: List<String> = listOf("Library", "Review"),
) {
//    var selectedIndex by remember { mutableStateOf(0) }
    TabRow(
        selectedTabIndex = selectedIndex,
        backgroundColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .height(56.dp)
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(10.dp))
            .then(modifier),
        indicator = { },
    ) {
        tabs.forEachIndexed { index, text ->
            val selected = selectedIndex == index
            Tab(
                modifier = if (selected) Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        MaterialTheme.colorScheme.background
                    )
                else Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        MaterialTheme.colorScheme.primary
                    ),
                selected = selected,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = text,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            )
        }
    }
}