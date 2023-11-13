package com.harissabil.anidex.ui.components.inputs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.harissabil.anidex.R
import com.harissabil.anidex.ui.components.button.NormalButton
import com.harissabil.anidex.ui.components.textfield.PasswordTextField
import com.harissabil.anidex.ui.components.textfield.UsernameTextField

@Composable
fun LoginInputs(
    username: String,
    password: String,
    onEmailOrMobileChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {

    // Login Inputs Section
    Column(modifier = Modifier.fillMaxWidth()) {

        // Username
        UsernameTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            value = username,
            onValueChange = onEmailOrMobileChange,
            label = stringResource(id = R.string.login_email_id_or_phone_label),
            isError = username.length > 20,
            errorText = "Username should be less than 20 characters",
            imeAction = ImeAction.Next
        )


        // Password
        PasswordTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            value = password,
            onValueChange = onPasswordChange,
            label = stringResource(id = R.string.login_password_label),
            isError = password.length > 20,
            errorText = "Password should be less than 20 characters",
            imeAction = ImeAction.Done
        )

        // Forgot Password
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .align(alignment = Alignment.End)
                .clickable {
                    onForgotPasswordClick.invoke()
                },
            text = stringResource(id = R.string.forgot_password),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium
        )

        // Login Submit Button
        NormalButton(
            modifier = Modifier.padding(top = 24.dp),
            text = stringResource(id = R.string.login_button_text),
            onClick = onSubmit,
        )

    }
}