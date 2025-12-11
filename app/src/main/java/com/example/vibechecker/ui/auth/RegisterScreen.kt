package com.example.vibechecker.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vibechecker.R
import com.example.vibechecker.ui.components.VibeButton
import com.example.vibechecker.ui.components.VibeTextField

@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") } // поки не відправляємо в Firebase Auth тільки email/pass
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isSuccess by viewModel.registerSuccess.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            Toast.makeText(context, "Акаунт створено!", Toast.LENGTH_SHORT).show()
            onRegisterClick()
        }
    }

    LaunchedEffect(error) {
        if (error != null) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Реєстрація",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    letterSpacing = 2.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            VibeTextField(value = name, onValueChange = { name = it }, label = "введіть ім'я")
            Spacer(modifier = Modifier.height(16.dp))

            VibeTextField(value = email, onValueChange = { email = it }, label = "email")
            Spacer(modifier = Modifier.height(16.dp))

            VibeTextField(value = password, onValueChange = { password = it }, label = "пароль", isPassword = true)
            Spacer(modifier = Modifier.height(16.dp))

            VibeTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "повторіть пароль", isPassword = true)

            Spacer(modifier = Modifier.height(32.dp))

            VibeButton(
                text = if (isLoading) "ЗАВАНТАЖЕННЯ..." else "ЗАРЕЄСТРУВАТИСЯ",
                onClick = {
                    if (!isLoading) {

                        viewModel.register(name, email, password, confirmPassword)
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialButton(icon = R.drawable.google_icon, onClick = {/* TODO: Google Reg */}, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(24.dp))
                SocialButton(icon = R.drawable.facebook_icon, onClick = {/* TODO: Facebook Reg */}, tint = MaterialTheme.colorScheme.primary)
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}