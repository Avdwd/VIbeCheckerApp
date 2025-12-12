package com.example.vibechecker.ui.navigation
import com.example.vibechecker.R


sealed class Screen(
    val route: String,
    val title: String? = null,
    val iconRes: Int? = null
) {

    data object Welcome : Screen("welcome_screen")
    data object Login : Screen("login_screen")
    data object Register : Screen("register_screen")
    data object MainContainer : Screen("main_container")


    data object Home : Screen("home_screen", "Головна", R.drawable.ic_nav_home)
    data object Diary : Screen("diary_screen", "Щоденник", R.drawable.ic_nav_diary)
    data object Profile : Screen("profile_screen", "ШІ", R.drawable.ic_nav_profile)


    data object AddEntry : Screen("add_entry_screen?mood={mood}") {
        fun createRoute(mood: Int? = null): String {
            return if (mood != null) "add_entry_screen?mood=$mood" else "add_entry_screen"
        }
    }

    data object EntryDetail : Screen("entry_detail_screen/{entryId}") {
        fun createRoute(entryId: Int) = "entry_detail_screen/$entryId"
    }
}