package co.uniquindio.qgame.model

import androidx.compose.ui.graphics.Color

enum class SimonColor(
    val baseColor: Color,
    val litColor: Color,
    val label: String,
    val toneFrequency: Int
) {
    GREEN(
        baseColor = Color(0xFF2E7D32),
        litColor = Color(0xFF69F0AE),
        label = "Verde",
        toneFrequency = 392
    ),
    RED(
        baseColor = Color(0xFFC62828),
        litColor = Color(0xFFFF5252),
        label = "Rojo",
        toneFrequency = 330
    ),
    YELLOW(
        baseColor = Color(0xFFF9A825),
        litColor = Color(0xFFFFFF00),
        label = "Amarillo",
        toneFrequency = 262
    ),
    BLUE(
        baseColor = Color(0xFF1565C0),
        litColor = Color(0xFF448AFF),
        label = "Azul",
        toneFrequency = 524
    )
}