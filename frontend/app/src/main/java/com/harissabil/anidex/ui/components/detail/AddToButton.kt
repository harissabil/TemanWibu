package com.harissabil.anidex.ui.components.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LibraryAdd
import androidx.compose.material.icons.outlined.LibraryAddCheck
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.harissabil.anidex.ui.theme.spacing

@Composable
fun AddToButton(
    isAddedToLibrary: Boolean,
    onAddToLibraryClick: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
        ) {
            ElevatedButton(
                onClick = { onAddToLibraryClick() },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(3f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Icon(
                    imageVector = if (!isAddedToLibrary) Icons.Outlined.LibraryAdd else Icons.Outlined.LibraryAddCheck,
                    contentDescription = "Add to Library Icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (!isAddedToLibrary) "Add to Library" else "Edit Library",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (isAddedToLibrary) {
                OutlinedButton(
                    onClick = onDeleteClicked,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Favorite Icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}
