package co.uniquindio.qgame.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import co.uniquindio.qgame.ui.screens.GameScreen
import co.uniquindio.qgame.ui.screens.HomeScreen
import co.uniquindio.qgame.ui.screens.ResultScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route,
        modifier = modifier
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(
                onStartGame = { playerName ->
                    val encoded = Uri.encode(playerName)
                    navController.navigate(NavRoutes.Game.createRoute(encoded))
                }
            )
        }

        composable(
            route = NavRoutes.Game.route,
            arguments = listOf(navArgument("playerName") { type = NavType.StringType })
        ) { backStackEntry ->
            val playerName = Uri.decode(backStackEntry.arguments?.getString("playerName") ?: "")
            GameScreen(
                playerName = playerName,
                onGameOver = { name, score ->
                    val encoded = Uri.encode(name)
                    navController.navigate(NavRoutes.Result.createRoute(encoded, score)) {
                        popUpTo(NavRoutes.Game.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = NavRoutes.Result.route,
            arguments = listOf(
                navArgument("playerName") { type = NavType.StringType },
                navArgument("score") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val playerName = Uri.decode(backStackEntry.arguments?.getString("playerName") ?: "")
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            ResultScreen(
                playerName = playerName,
                score = score,
                onPlayAgain = {
                    val encoded = Uri.encode(playerName)
                    navController.navigate(NavRoutes.Game.createRoute(encoded)) {
                        popUpTo(NavRoutes.Result.route) { inclusive = true }
                    }
                },
                onGoHome = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}