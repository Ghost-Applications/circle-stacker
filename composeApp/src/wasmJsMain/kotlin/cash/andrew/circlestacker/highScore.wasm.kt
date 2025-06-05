package cash.andrew.circlestacker

import kotlinx.browser.window

actual suspend fun loadHighScore(): Int {
    return window.localStorage.getItem(HIGH_SCORE_FILE)?.toInt() ?: 0
}

actual suspend fun saveHighScore(score: Int) {
    window.localStorage.setItem(HIGH_SCORE_FILE, score.toString())
}
