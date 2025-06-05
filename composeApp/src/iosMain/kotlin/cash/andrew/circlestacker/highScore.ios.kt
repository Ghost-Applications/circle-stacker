package cash.andrew.circlestacker

import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

actual suspend fun loadHighScore(): Int {
    return try {
        withContext(Dispatchers.Default) {
            val paths = NSSearchPathForDirectoriesInDomains(
                directory = NSApplicationSupportDirectory,
                domainMask = NSUserDomainMask,
                expandTilde = true
            )
            val appSupportDir = paths.firstOrNull() as? String
            val myAppDir = appSupportDir?.let { "$it/CircleStacker" }?.toPath() ?: run {
                Napier.e { "Couldn't get app directory" }
                return@withContext 0
            }

            FileSystem.SYSTEM.read(myAppDir.resolve(HIGH_SCORE_FILE)) {
                readUtf8().toInt()
            }
        }
    } catch (e: Exception) {
        Napier.e("loadHighScore", e)
        0
    }
}

actual suspend fun saveHighScore(score: Int) {
    try {
        withContext(Dispatchers.Default) {
            val paths = NSSearchPathForDirectoriesInDomains(
                directory = NSApplicationSupportDirectory,
                domainMask = NSUserDomainMask,
                expandTilde = true
            )
            val appSupportDir = paths.firstOrNull() as? String
            val myAppDir = appSupportDir?.let { "$it/CircleStacker" }?.toPath() ?: run {
                Napier.e { "Couldn't get app directory" }
                return@withContext
            }

            FileSystem.SYSTEM.createDirectories(myAppDir)
            FileSystem.SYSTEM.write(myAppDir.resolve(HIGH_SCORE_FILE)) {
                writeUtf8(score.toString())
            }
        }
    } catch (e: Exception) {
        Napier.e("GameOver", e, "Error saving high score")
    }
}