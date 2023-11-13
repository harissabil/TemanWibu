package com.harissabil.anidex.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.harissabil.anidex.R
import com.harissabil.anidex.ui.screen.auth.AuthViewModel
import com.harissabil.anidex.ui.screen.detail.CardSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onLogoutClick: () -> Unit
) {
    val userState by viewModel.userState.collectAsState()
    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = userState.username,
//                        style = MaterialTheme.typography.titleLarge,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis,
//                        modifier = Modifier.padding(end = 16.dp)
//                    )
//                },
//            )
//        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding)
                .padding(vertical = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(space = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileContent(
                    name = userState.name,
                    username = userState.username,
                    email = userState.email,
                    password = userState.password
                )
            }
            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Log out Icon",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Log out",
                )
            }
        }
    }
}

@Composable
fun ProfileContent(
    name: String,
    username: String,
    email: String,
    password: String
) {
    Image(
        painter = painterResource(id = R.drawable.no_profile),
        contentDescription = "Profile picture",
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
    )
    Text(
        text = name,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.SemiBold
        ),
    )
    CardSection {
        Row {
            Text(text = "Username")
            Text(
                text = username,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row {
            Text(text = "Email")
            Text(
                text = email,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row {
            var isPasswordVisible by rememberSaveable {
                mutableStateOf(false)
            }
            Text(text = "Password")
            Text(
                text = if (!isPasswordVisible) password.maskPassword() else password,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        isPasswordVisible = !isPasswordVisible
                    }
            )
        }
    }
}

fun String.maskPassword(): String {
    val maskedPassword = StringBuilder()
    for (i in this.indices) {
        maskedPassword.append('*')
    }
    return maskedPassword.toString()
}