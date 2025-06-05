package cash.andrew.circlestacker

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(viewModel: MainViewModel = viewModel { MainViewModel() },) {
    MaterialTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            Game(viewModel)
        }
    }
}

fun CoroutineScope.restartGame(
    minDimension: Float,
    maxRadiusSize: Float,
    radius: Animatable<Float, AnimationVector1D>,
    viewModel: MainViewModel
) {
    launch {
        restartRadiusAnimation(
            minDimension = minDimension,
            targetValue = maxRadiusSize,
            animation = radius,
            viewModel = viewModel
        )
    }

    viewModel.startPlaying(maxRadiusSize)
}

@Composable
fun Game(viewModel: MainViewModel) {
    val gameState by viewModel.appState.collectAsState()

    var maxRadiusSize by remember { mutableFloatStateOf(0f) }
    var minDimension by remember { mutableFloatStateOf(0f) }
    var highScore by remember { mutableIntStateOf(0) }
    val radius = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(gameState) {
        highScore = loadHighScore()
    }

    val restartGame = {
        coroutineScope.restartGame(
            minDimension = minDimension,
            maxRadiusSize = maxRadiusSize,
            radius = radius,
            viewModel = viewModel
        )
    }

    when (val state = gameState) {
        GameState.BeforePlay -> BeginGame(
            highScore = highScore,
            minDimensionUpdate = {
                minDimension = it
                maxRadiusSize = it / 2
            },
            onClick = restartGame
        )

        is GameState.GameOver -> GameOver(
            highScore = highScore,
            gameState = state,
            onClick = {
                highScore = it ?: highScore
                restartGame()
            }
        )

        is GameState.Playing -> {
            GameRunning(gameState = state, radius = radius.value, highScore = highScore) {
                coroutineScope.launch {
                    restartRadiusAnimation(
                        minDimension = minDimension,
                        targetValue = radius.value,
                        animation = radius,
                        viewModel = viewModel
                    )
                }

                val nextColor = generateSequence { randomColor() }
                    .first { it != state.currentCircleColor }

                val points = state.points + 1

                if (points > highScore) {
                    highScore = points
                }
                viewModel.createNextCircle(radius.value, nextColor, points)
            }
        }
    }
}

@Composable
fun BeginGame(
    highScore: Int,
    minDimensionUpdate: (minDimension: Float) -> Unit,
    onClick: () -> Unit
) {
    val color by remember { mutableStateOf(randomColor()) }

    Points(0, highScore)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }
        ) {
            minDimensionUpdate(size.minDimension)
            drawCircle(
                color = color,
                radius = size.minDimension / 2
            )
        }
        Text(
            text = "Tap to start!",
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun GameRunning(
    highScore: Int,
    gameState: GameState.Playing,
    radius: Float,
    circleClickUpdate: () -> Unit,
) {
    Points(gameState.points, highScore)
    Box(modifier = Modifier.fillMaxSize()) {
        OldCircles(listOfCircles = gameState.oldCircles)
        NewCircle(
            color = gameState.currentCircleColor,
            radius = radius,
            onClick = circleClickUpdate
        )
    }
}

@Composable
fun GameOver(
    highScore: Int,
    gameState: GameState.GameOver,
    onClick: (newHighScore: Int?) -> Unit
) {
    var newHighScore by remember { mutableStateOf(false) }

    LaunchedEffect(gameState) {
        newHighScore = gameState.points > highScore
        if (newHighScore) {
            saveHighScore(gameState.points)
        }
    }

    Points(points = gameState.points, highScore = highScore)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = { onClick(if (newHighScore) gameState.points else null) }),
        contentAlignment = Alignment.Center
    ) {
        OldCircles(listOfCircles = gameState.circles)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (newHighScore) {
                Text(
                    text = "New high score!",
                    style = MaterialTheme.typography.headlineMedium,
                )
            } else {
                Text(
                    text = "Game Over!",
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
            Text(
                text = "Tap to Play Again",
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

@Composable
fun Points(points: Int, highScore: Int) {
    Row(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = "Points: $points",
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = "High Score: $highScore",
            style = MaterialTheme.typography.titleLarge
        )

    }
}

@Composable
fun OldCircles(listOfCircles: List<GameState.CircleData>) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        listOfCircles.forEach { (radius, color) ->
            drawCircle(
                color = color,
                radius = radius
            )
        }
    }
}

@Composable
fun NewCircle(
    color: Color,
    radius: Float,
    onClick: () -> Unit,
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .clickable {
                onClick()
            }
    ) {
        drawCircle(
            color = color,
            radius = radius
        )
    }
}

private const val MAX_ANIMATION_DURATION_MS = 5 * 1000

suspend fun restartRadiusAnimation(
    minDimension: Float,
    targetValue: Float,
    animation: Animatable<Float, AnimationVector1D>,
    viewModel: MainViewModel
) {
    val animationDuration: Float = (targetValue / minDimension) * MAX_ANIMATION_DURATION_MS

    animation.snapTo(0f)
    animation.animateTo(
        targetValue = targetValue,
        animationSpec = tween(
            durationMillis = animationDuration.toInt(),
            easing = LinearEasing
        )
    )
    viewModel.gameOver()
}
