package view.gameplay

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import model.gameField.GameField
import org.jetbrains.skia.Font
import view.navigation.AppState

//TODO: fix overlay with other elements
@Composable
fun GameField(gameField: GameField) {
    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        color = Color.Black.toArgb()
    }

    Canvas(modifier = Modifier.size(width = 500f.dp, height = 400f.dp)) {
        gameField.path.getLines().forEach {
            drawLine(
                start = it.first,
                end = it.second,
                color = Color.DarkGray,
                strokeWidth = 2f
            )
        }

        gameField.path.base.let { base ->
            drawCircle(radius = base.radius, center = base.center, color = Color.Gray)
            drawIntoCanvas {
                it.nativeCanvas.drawString(
                    s = base.health.value.toString(),
                    x = base.center.x,
                    y = base.center.y,
                    font = Font(),
                    paint = textPaint
                )
            }
        }

        drawIntoCanvas {
            it.nativeCanvas.drawString(
                s = AppState.translate("enemy_counter")
                    .format(gameField.enemiesOnField.size, gameField.enemiesIncoming.size),
                x = 100f,
                y = 20f,
                font = Font(),
                paint = textPaint
            )
        }

        gameField.enemiesOnField.forEach { enemy ->
            drawIntoCanvas {
                it.nativeCanvas.drawString(
                    s = enemy.toString(),
                    x = enemy.position.value.x,
                    y = enemy.position.value.y,
                    font = Font(),
                    paint = textPaint
                )
            }
        }
    }
}