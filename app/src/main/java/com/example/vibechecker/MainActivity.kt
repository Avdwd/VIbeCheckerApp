package com.example.vibechecker

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.vibechecker.data.notification.NotificationScheduler
import com.example.vibechecker.ui.navigation.AppNavGraph
import com.example.vibechecker.ui.navigation.Screen
import com.example.vibechecker.ui.theme.VIbeCheckerTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

import javax.inject.Inject

@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    // Отримуємо доступ до Firebase
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    // Реєструємо запит дозволу
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Якщо дали дозвіл - плануємо нагадування
            NotificationScheduler.scheduleDailyReminder(this)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Для Android 13+ просимо дозвіл
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // Для старих версій просто запускаємо
            NotificationScheduler.scheduleDailyReminder(this)
        }

        var isCheckingAuth = true
        val startDestination = if (firebaseAuth.currentUser != null) {
            Screen.MainContainer.route
        } else {
            Screen.Welcome.route
        }
        isCheckingAuth = false

        splashScreen.setKeepOnScreenCondition {
            isCheckingAuth
        }

        setContent {
            VIbeCheckerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavGraph(startDestination = startDestination)
                }
            }
        }
    }
}
