package cash.andrew.circlestacker

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
sealed interface GameState {

    @Immutable
    data object BeforePlay: GameState

    @Immutable
    data class Playing(
        val maxRadius: Float,
        val oldCircles: List<CircleData> = emptyList(),
        val points: Int = 0,
        val currentCircleColor: Color = randomColor(),
    ): GameState

    @Immutable
    data class GameOver(
        val circles: List<CircleData>,
        val points: Int
    ): GameState

    @Immutable
    data class CircleData(
        val radius: Float,
        val color: Color,
    )
}
