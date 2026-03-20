package co.uniquindio.qgame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.uniquindio.qgame.model.GamePhase
import co.uniquindio.qgame.model.GameUiState
import co.uniquindio.qgame.model.SimonColor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var sequenceJob: Job? = null

    companion object {
        private const val LIGHT_DURATION_MS = 600L
        private const val PAUSE_BETWEEN_MS = 350L
        private const val PLAYER_FLASH_MS = 250L
        private const val DELAY_BEFORE_NEXT_ROUND = 1000L
        private const val MAX_SEQUENCE_LENGTH = 10
    }

    fun startGame(playerName: String) {
        sequenceJob?.cancel()
        _uiState.value = GameUiState(
            playerName = playerName,
            phase = GamePhase.SHOWING_SEQUENCE,
            round = 1
        )
        addColorAndShowSequence()
    }

    private fun addColorAndShowSequence() {
        val newColor = SimonColor.entries.random()
        _uiState.update { it.copy(sequence = it.sequence + newColor) }
        showSequence()
    }

    private fun showSequence() {
        sequenceJob?.cancel()
        sequenceJob = viewModelScope.launch {
            _uiState.update { it.copy(phase = GamePhase.SHOWING_SEQUENCE, litButton = null) }
            delay(500L)

            val sequence = _uiState.value.sequence
            for (color in sequence) {
                _uiState.update { it.copy(litButton = color) }
                delay(LIGHT_DURATION_MS)
                _uiState.update { it.copy(litButton = null) }
                delay(PAUSE_BETWEEN_MS)
            }

            _uiState.update {
                it.copy(
                    phase = GamePhase.PLAYER_INPUT,
                    playerInputIndex = 0,
                    litButton = null
                )
            }
        }
    }

    fun onPlayerTap(color: SimonColor) {
        val state = _uiState.value
        if (state.phase != GamePhase.PLAYER_INPUT) return

        val expected = state.sequence[state.playerInputIndex]

        if (color != expected) {
            _uiState.update { it.copy(phase = GamePhase.GAME_OVER, litButton = color) }
            viewModelScope.launch {
                delay(PLAYER_FLASH_MS)
                _uiState.update { it.copy(litButton = null) }
            }
            return
        }

        // Flash the correct button
        _uiState.update { it.copy(litButton = color, phase = GamePhase.SHOWING_SEQUENCE) }

        val newIndex = state.playerInputIndex + 1

        viewModelScope.launch {
            delay(PLAYER_FLASH_MS)
            _uiState.update { it.copy(litButton = null) }

            if (newIndex == state.sequence.size) {
                val newScore = state.score + 1

                if (state.sequence.size >= MAX_SEQUENCE_LENGTH) {
                    _uiState.update {
                        it.copy(score = newScore, phase = GamePhase.GAME_OVER)
                    }
                    return@launch
                }

                _uiState.update {
                    it.copy(
                        score = newScore,
                        round = state.round + 1,
                        playerInputIndex = 0,
                        phase = GamePhase.SHOWING_SEQUENCE
                    )
                }

                delay(DELAY_BEFORE_NEXT_ROUND)
                addColorAndShowSequence()
            } else {
                _uiState.update {
                    it.copy(
                        playerInputIndex = newIndex,
                        phase = GamePhase.PLAYER_INPUT
                    )
                }
            }
        }
    }

    fun resetGame() {
        sequenceJob?.cancel()
        _uiState.value = GameUiState()
    }

    override fun onCleared() {
        super.onCleared()
        sequenceJob?.cancel()
    }
}