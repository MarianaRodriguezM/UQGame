package co.uniquindio.qgame.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.uniquindio.qgame.ui.theme.AccentPink
import co.uniquindio.qgame.ui.theme.DarkBackground
import co.uniquindio.qgame.ui.theme.DarkSurface
import co.uniquindio.qgame.ui.theme.GoldAccent
import co.uniquindio.qgame.ui.theme.TextGray

@Composable
fun ResultScreen(
    playerName: String,
    score: Int,
    onPlayAgain: () -> Unit,
    onGoHome: () -> Unit
) {
    val performanceMessage = when {
        score >= 10 -> "Perfecto! Eres un maestro!"
        score >= 7 -> "Excelente! Muy bien hecho!"
        score >= 4 -> "Buen intento! Sigue practicando!"
        score >= 1 -> "No esta mal, puedes mejorar!"
        else -> "Sigue intentando!"
    }

    val performanceEmoji = when {
        score >= 10 -> "🏆"
        score >= 7 -> "⭐"
        score >= 4 -> "👏"
        score >= 1 -> "💪"
        else -> "🎮"
    }

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
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Trophy/emoji
            Text(
                text = performanceEmoji,
                fontSize = 72.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Juego Terminado",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black
                ),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Player name
            Text(
                text = playerName,
                style = MaterialTheme.typography.headlineMedium,
                color = AccentPink
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Score card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.06f))
                    .padding(vertical = 32.dp, horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "PUNTAJE",
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 4.sp
                        ),
                        color = TextGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "$score",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = 64.sp,
                                fontWeight = FontWeight.Black
                            ),
                            color = GoldAccent
                        )
                        Text(
                            text = " / 10",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextGray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Score bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(score / 10f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(AccentPink, GoldAccent)
                                    )
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = performanceMessage,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = TextGray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Buttons
            Button(
                onClick = onPlayAgain,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPink),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "JUGAR DE NUEVO",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 16.sp,
                        letterSpacing = 2.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onGoHome,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "INICIO",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 16.sp,
                        letterSpacing = 2.sp
                    )
                )
            }
        }
    }
}