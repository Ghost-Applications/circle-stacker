package cash.andrew.circlestacker

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _appState: MutableStateFlow<GameState> = MutableStateFlow(GameState.BeforePlay)
    val appState: StateFlow<GameState> = _appState

    fun startPlaying(maxRadius: Float) {
        viewModelScope.launch {
            _appState.emit(GameState.Playing(maxRadius))
        }
    }

    fun createNextCircle(radius: Float, nextColor: Color, points: Int) {
        viewModelScope.launch {
            when (val state = _appState.value) {
                is GameState.Playing -> {
                    val nextState = state.copy(
                        oldCircles = state.oldCircles + GameState.CircleData(
                            radius = radius,
                            color = state.currentCircleColor,
                        ),
                        currentCircleColor = nextColor,
                        points = points
                    )
                    _appState.emit(nextState)
                }

                else -> Unit
            }
        }
    }

    fun gameOver() {
        viewModelScope.launch {
            // if we were in game over mode and the animation ends
            // skip over the second game over attempt
            val state = (_appState.value as? GameState.Playing) ?: return@launch

            val (r, oldCircles, points, color) = state
            _appState.emit(
                GameState.GameOver(
                    circles = oldCircles + GameState.CircleData(
                        radius = r,
                        color = color
                    ),
                    points = points
                )
            )
        }
    }
}
