package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.AppManagementScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LobbyScreen
import com.example.ui.screens.SettingsScreen
import com.example.viewmodels.GameViewModel
import com.example.viewmodels.SystemStatsViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val gameViewModel: GameViewModel = viewModel()
    val statsViewModel: SystemStatsViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        statsViewModel.startMonitoring(context)
    }

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen(
                gameViewModel = gameViewModel,
                statsViewModel = statsViewModel,
                onNavigateToLobby = { navController.navigate("lobby") },
                onNavigateToSettings = { navController.navigate("settings") },
                onNavigateToAppManagement = { navController.navigate("app_management") }
            )
        }
        composable("lobby") {
            LobbyScreen(
                gameViewModel = gameViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAppManagement = { navController.navigate("app_management") }
            )
        }
        composable("settings") {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("app_management") {
            AppManagementScreen(
                gameViewModel = gameViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
