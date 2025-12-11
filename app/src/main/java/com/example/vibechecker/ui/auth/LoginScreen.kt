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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vibechecker.R
import com.example.vibechecker.ui.components.VibeButton
import com.example.vibechecker.ui.components.VibeTextField
import com.example.vibechecker.ui.theme.VIbeCheckerTheme

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel() // Підключаємо ViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Слідкуємо за станами з ViewModel
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isSuccess by viewModel.loginSuccess.collectAsState()

    val context = LocalContext.current

    // УСПІХ
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            onLoginClick()
        }
    }

    //ОБРОБНИК ПОМИЛОК
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Авторизація",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    letterSpacing = 2.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(48.dp))

            VibeTextField(
                value = email,
                onValueChange = { email = it },
                label = "email"
            )

            Spacer(modifier = Modifier.height(16.dp))

            VibeTextField(
                value = password,
                onValueChange = { password = it },
                label = "пароль",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ВХІД
            VibeButton(
                text = if (isLoading) "ЗАВАНТАЖЕННЯ..." else "УВІЙТИ",
                onClick = {

                    if (!isLoading) {
                        viewModel.login(email, password)
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Соцмережі заглушки
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialButton(
                    icon = R.drawable.google_icon,
                    onClick = { /* TODO: Google Auth */ },
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(24.dp))

                SocialButton(
                    icon = R.drawable.facebook_icon,
                    onClick = { /* TODO: Facebook Auth */ },
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // індикатор завантаження по центру екрану
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SocialButton(
    icon: Int,
    onClick: () -> Unit,
    tint: Color = Color.Unspecified
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.size(60.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            tint = tint
        )
    }
}