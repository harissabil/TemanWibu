package com.harissabil.anidex.ui.components.detail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import com.harissabil.anidex.ui.components.textfield.ErrorTextInputField
import com.harissabil.anidex.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewBottomSheet(
    modifier: Modifier = Modifier,
    review: String,
    onReviewChange: (String) -> Unit,
    onSubmitClicked: () -> Unit,
    isError: Boolean = false,
    errorText: String = "",
    onDismiss: () -> Unit,
    modalBottomSheetState: SheetState,
    rating: Float,
    onRatingChange: (Float) -> Unit,
    onWishlistClick: () -> Unit,
    onWatchlistClick: () -> Unit,
    onWatchedClick: () -> Unit,
) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = modalBottomSheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Write a review",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                RatingBar(
                    value = rating,
                    style = RatingBarStyle.Stroke(),
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
                    shape = RoundedCornerShape(8.dp)
                )
                Button(
                    onClick = onSubmitClicked,
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(
                        text = "Submit Review",
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    } else {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = modalBottomSheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
            windowInsets = WindowInsets.ime
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add to Library",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium),
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
                                contentDescription = "Favorite Icon",
                                tint = MaterialTheme.colorScheme.onBackground
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
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                        OutlinedButton(
                            onClick = { onWatchedClick() },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1F)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = "Watched Icon",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                RatingBar(
                    value = rating,
                    style = RatingBarStyle.Stroke(),
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
                    shape = RoundedCornerShape(8.dp)
                )
                Button(
                    onClick = onSubmitClicked,
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(
                        text = "Submit Review",
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Spacer(
                    modifier = Modifier.height(
                        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                    )
                )
            }
        }
    }
}
