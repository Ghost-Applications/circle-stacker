package cash.andrew.circlestacker

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual suspend fun loadHighScore(): Int {
    return try {
        withContext(Dispatchers.IO) {
            CircleStackerApp.context.openFileInput(HIGH_SCORE_FILE).use { f ->
                f.bufferedReader()
                    .readText()
                    .toIntOrNull() ?: 0
            }
        }
    } catch (e: Exception) {
        Log.e("loadHighScore", null, e)
        0
    }
}

actual suspend fun saveHighScore(score: Int) {
    try {
        withContext(Dispatchers.IO) {
            CircleStackerApp.context.openFileOutput(HIGH_SCORE_FILE, Context.MODE_PRIVATE).use {
                it.write(score.toString().toByteArray())
            }
        }
    } catch (e: Exception) {
        Log.e("GameOver", "Error saving high score", e)
    }}
