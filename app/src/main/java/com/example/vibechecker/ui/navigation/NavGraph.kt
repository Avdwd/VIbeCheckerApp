package com.example.vibechecker.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vibechecker.ui.auth.LoginScreen
import com.example.vibechecker.ui.auth.RegisterScreen
import com.example.vibechecker.ui.auth.WelcomeScreen
import com.example.vibechecker.ui.diary.AddEntryScreen
import com.example.vibechecker.ui.diary.EntryDetailScreen
import com.example.vibechecker.ui.home.HomeScreen
import com.example.vibechecker.ui.home.MainScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = {

                    navController.navigate(Screen.MainContainer.route) {

                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }


        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterClick = {

                    navController.navigate(Screen.MainContainer.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }


        composable(Screen.MainContainer.route) {
            MainScreen(
                onNavigateToAddEntry = {
                    navController.navigate(Screen.AddEntry.createRoute(null))
                },
                onNavigateToAddEntryWithMood = { mood ->
                    // Передаємо вибраний настрій
                    navController.navigate(Screen.AddEntry.createRoute(mood))
                },
                onNavigateToDetail = { entryId ->
                    navController.navigate(Screen.EntryDetail.createRoute(entryId))
                },

                onLogout = {
                    // Переходимо на екран Логіну
                    navController.navigate(Screen.Login.route) {

                        popUpTo(0)
                    }
                }
            )
        }

        composable(
            route = Screen.AddEntry.route,
            arguments = listOf(
                navArgument("mood") {
                    type = NavType.StringType // Передаємо як рядок для простоти
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            AddEntryScreen(
                onBackClick = { navController.popBackStack() }
            )
        }


        //Екран деталей
        composable(
            route = Screen.EntryDetail.route,
            arguments = listOf(navArgument("entryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getInt("entryId") ?: 0

            EntryDetailScreen(
                entryId = entryId,
                onBackClick = { navController.popBackStack() }
            )
        }

    }
}