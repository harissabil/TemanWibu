package com.harissabil.anidex.ui.components.detail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import com.harissabil.anidex.ui.components.textfield.ErrorTextInputField
import com.harissabil.anidex.ui.theme.spacing

@Composable
fun AddToLibraryDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    review: String,
    onReviewChange: (String) -> Unit,
    isError: Boolean = false,
    errorText: String = "",
    rating: Float,
    onRatingChange: (Float) -> Unit,
    onWishlistClick: () -> Unit,
    onWatchlistClick: () -> Unit,
    onFinishedClick: () -> Unit,
    status: String,
    isAddedToLibrary: Boolean,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (!isAddedToLibrary) "Add to Library" else "Edit Library",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = { onWishlistClick() },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1F),
                        ) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "Wishlist Icon",
                                tint = if (status == "WISHLIST") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                            )
                        }
                        OutlinedButton(
                            onClick = { onWatchlistClick() },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1F),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.WatchLater,
                                contentDescription = "Watchlist Icon",
                                tint = if (status == "WATCHLIST") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                            )
                        }
                        OutlinedButton(
                            onClick = { onFinishedClick() },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1F)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = "Finished Icon",
                                tint = if (status == "FINISHED") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                RatingBar(
                    value = rating,
                    style = RatingBarStyle.Fill(
                        activeColor = MaterialTheme.colorScheme.primary,
                        inActiveColor = MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.3F
                        )
                    ),
                    onValueChange = onRatingChange,
                    onRatingChanged = {
                        Log.d("TAG", "onRatingChanged: $it")
                    },
                    stepSize = StepSize.HALF,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = review, onValueChange = onReviewChange,
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                    }),
                    isError = isError,
                    supportingText = {
                        if (isError) {
                            ErrorTextInputField(text = errorText)
                        }
                    },
                    maxLines = 5,
                    minLines = 3,
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = {
                        Text(
                            text = "Write a review",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = onConfirmation,
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}