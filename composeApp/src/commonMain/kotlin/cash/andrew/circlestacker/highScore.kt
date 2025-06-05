package cash.andrew.circlestacker

const val HIGH_SCORE_FILE = "highscore.txt"

expect suspend fun loadHighScore(): Int

expect suspend fun saveHighScore(score: Int)
