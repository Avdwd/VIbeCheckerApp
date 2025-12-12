package com.example.vibechecker.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vibechecker.ui.diary.DiaryScreen
import com.example.vibechecker.ui.home.HomeScreen
import com.example.vibechecker.ui.navigation.Screen
import com.example.vibechecker.ui.profile.ProfileScreen

@Composable
fun MainScreen(
    onNavigateToAddEntry: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToAddEntryWithMood: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val bottomNavController = rememberNavController()

    val items = listOf(
        Screen.Home,
        Screen.Diary,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        icon = {

                            screen.iconRes?.let { iconId ->
                                Icon(
                                    painter = painterResource(id = iconId),
                                    contentDescription = screen.title
                                )
                            }
                        },
                        label = {
                            if (isSelected) {
                                Text(text = screen.title ?: "", fontWeight = FontWeight.Bold)
                            }
                        },
                        selected = isSelected,
                        onClick = {
                            bottomNavController.navigate(screen.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(onMoodClick = onNavigateToAddEntryWithMood) }
            composable(Screen.Diary.route) { DiaryScreen(onItemClick = onNavigateToDetail,onFabClick = onNavigateToAddEntry) }
            composable(Screen.Profile.route) { ProfileScreen(onLogout = onLogout) }
        }
    }
}