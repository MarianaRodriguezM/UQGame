package co.uniquindio.qgame.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.uniquindio.qgame.model.SimonColor

@Composable
fun SimonButton(
    simonColor: SimonColor,
    isLit: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color by animateColorAsState(
        targetValue = if (isLit) simonColor.litColor else simonColor.baseColor,
        animationSpec = tween(durationMillis = 200),
        label = "buttonColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (isLit) 1.06f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "buttonScale"
    )

    val shadowColor = if (isLit) simonColor.litColor else Color.Transparent
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .shadow(
                elevation = if (isLit) 16.dp else 4.dp,
                shape = shape,
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
            .clip(shape)
            .background(color)
            .then(
                if (enabled) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            )
    )
}

@Composable
fun SimonButtonGrid(
    litButton: SimonColor?,
    enabled: Boolean,
    onButtonClick: (SimonColor) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Column(
        modifier = modifier,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            SimonButton(
                simonColor = SimonColor.GREEN,
                isLit = litButton == SimonColor.GREEN,
                enabled = enabled,
                onClick = { onButtonClick(SimonColor.GREEN) },
                modifier = Modifier.weight(1f).fillMaxSize()
            )
            SimonButton(
                simonColor = SimonColor.RED,
                isLit = litButton == SimonColor.RED,
                enabled = enabled,
                onClick = { onButtonClick(SimonColor.RED) },
                modifier = Modifier.weight(1f).fillMaxSize()
            )
        }
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            SimonButton(
                simonColor = SimonColor.YELLOW,
                isLit = litButton == SimonColor.YELLOW,
                enabled = enabled,
                onClick = { onButtonClick(SimonColor.YELLOW) },
                modifier = Modifier.weight(1f).fillMaxSize()
            )
            SimonButton(
                simonColor = SimonColor.BLUE,
                isLit = litButton == SimonColor.BLUE,
                enabled = enabled,
                onClick = { onButtonClick(SimonColor.BLUE) },
                modifier = Modifier.weight(1f).fillMaxSize()
            )
        }
    }
}