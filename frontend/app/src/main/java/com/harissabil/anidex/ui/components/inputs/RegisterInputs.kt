package com.harissabil.anidex.ui.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.harissabil.anidex.R
import com.harissabil.anidex.ui.components.button.NormalButton
import com.harissabil.anidex.ui.components.textfield.PasswordTextField
import com.harissabil.anidex.ui.components.textfield.UsernameTextField

@Composable
fun RegisterInputs(
    username: String,
    password: String,
    name: String,
    email: String,
    onNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit
) {

    // Login Inputs Section
    Column(modifier = Modifier.fillMaxWidth()) {

        // Name
        UsernameTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            value = name,
            onValueChange = onNameChange,
            label = "Name",
            isError = name.length > 40,
            errorText = "Name should be less than 40 characters",
            imeAction = ImeAction.Next
        )

        // Username
        UsernameTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            value = username,
            onValueChange = onUsernameChange,
            label = stringResource(id = R.string.login_email_id_or_phone_label),
            isError = username.length > 20,
            errorText = "Username should be less than 20 characters",
            imeAction = ImeAction.Next
        )

        // Email
        UsernameTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            value = email,
            onValueChange = onEmailChange,
            label = "Email",
            isError = username.length > 40,
            errorText = "Username should be less than 40 characters",
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

        // Register Submit Button
        NormalButton(
            modifier = Modifier.padding(top = 24.dp),
            text = "Register",
            onClick = onSubmit
        )

    }
}