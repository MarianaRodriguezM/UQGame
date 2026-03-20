package co.uniquindio.qgame.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.uniquindio.qgame.model.GamePhase
import co.uniquindio.qgame.model.SimonColor
import co.uniquindio.qgame.ui.components.SimonButtonGrid
import co.uniquindio.qgame.ui.components.SoundManager
import co.uniquindio.qgame.ui.theme.AccentPink
import co.uniquindio.qgame.ui.theme.DarkBackground
import co.uniquindio.qgame.ui.theme.DarkSurface
import co.uniquindio.qgame.ui.theme.GoldAccent
import co.uniquindio.qgame.ui.theme.TextGray
import co.uniquindio.qgame.viewmodel.GameViewModel

@Composable
fun GameScreen(
    playerName: String,
    onGameOver: (String, Int) -> Unit,
    viewModel: GameViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // Track previous litButton to play sound on change
    var previousLitButton by remember { mutableStateOf<SimonColor?>(null) }

    LaunchedEffect(Unit) {
        viewModel.startGame(playerName)
    }

    // Play sound whenever a button lights up
    LaunchedEffect(state.litButton) {
        val currentLit = state.litButton
        if (currentLit != null && currentLit != previousLitButton) {
            if (state.phase == GamePhase.GAME_OVER) {
                SoundManager.playErrorTone()
            } else {
                SoundManager.playTone(currentLit)
            }
        }
        previousLitButton = currentLit
    }

    LaunchedEffect(state.phase) {
        if (state.phase == GamePhase.GAME_OVER) {
            kotlinx.coroutines.delay(600)
            onGameOver(playerName, state.score)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "statusPulse")
    val statusAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "statusAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkSurface)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = playerName,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Ronda ${state.round}/10",
                        style = MaterialTheme.typography.labelLarge,
                        color = GoldAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Score
            Text(
                text = "Puntaje: ${state.score}",
                style = MaterialTheme.typography.bodyLarge,
                color = TextGray
            )

            Spacer(modifier = Modifier.weight(1f))

            // Simon button grid
            SimonButtonGrid(
                litButton = state.litButton,
                enabled = state.phase == GamePhase.PLAYER_INPUT,
                onButtonClick = { color ->
                    viewModel.onPlayerTap(color)
                },
                modifier = Modifier
                    .widthIn(max = 340.dp)
                    .aspectRatio(1f)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Status text
            val statusText = when (state.phase) {
                GamePhase.SHOWING_SEQUENCE -> "Observa la secuencia..."
                GamePhase.PLAYER_INPUT -> "Tu turno!"
                GamePhase.GAME_OVER -> "Juego terminado"
                GamePhase.WAITING_TO_START -> "Preparando..."
            }

            val statusColor = when (state.phase) {
                GamePhase.SHOWING_SEQUENCE -> GoldAccent
                GamePhase.PLAYER_INPUT -> Color(0xFF69F0AE)
                GamePhase.GAME_OVER -> AccentPink
                GamePhase.WAITING_TO_START -> TextGray
            }

            Text(
                text = statusText,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = statusColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(
                    if (state.phase == GamePhase.SHOWING_SEQUENCE) statusAlpha else 1f
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}