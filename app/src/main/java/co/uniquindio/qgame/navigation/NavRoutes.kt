package co.uniquindio.qgame.navigation

sealed class NavRoutes(val route: String) {
    data object Home : NavRoutes("home")
    data object Game : NavRoutes("game/{playerName}") {
        fun createRoute(playerName: String) = "game/$playerName"
    }
    data object Result : NavRoutes("result/{playerName}/{score}") {
        fun createRoute(playerName: String, score: Int) = "result/$playerName/$score"
    }
}