package github.masterj3y.subscene.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import github.masterj3y.subscene.navigation.MainNavigation

@Composable
fun MainScreen() {

    val navController = rememberNavController()

    MainNavigation(navController = navController)
}