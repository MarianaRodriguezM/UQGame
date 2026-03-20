package co.uniquindio.qgame.model

enum class GamePhase {
    WAITING_TO_START,
    SHOWING_SEQUENCE,
    PLAYER_INPUT,
    GAME_OVER
}

data class GameUiState(
    val playerName: String = "",
    val sequence: List<SimonColor> = emptyList(),
    val litButton: SimonColor? = null,
    val playerInputIndex: Int = 0,
    val phase: GamePhase = GamePhase.WAITING_TO_START,
    val score: Int = 0,
    val round: Int = 0
)