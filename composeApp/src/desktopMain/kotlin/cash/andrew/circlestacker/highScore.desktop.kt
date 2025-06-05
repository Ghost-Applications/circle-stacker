package cash.andrew.circlestacker

import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import java.util.Locale

actual suspend fun loadHighScore(): Int {
    return try {
        withContext(Dispatchers.IO) {
            val path = getAppSaveFilePath()
            FileSystem.SYSTEM.read(path) {
                readUtf8().toInt()
            }
        }
    } catch (e: Exception) {
        Napier.e("loadHighScore", e, "Error loading high score")
        0
    }
}

actual suspend fun saveHighScore(score: Int) {
    try {
        withContext(Dispatchers.IO) {
            FileSystem.SYSTEM.write(getAppSaveFilePath()) {
                writeUtf8(score.toString())
            }
        }
    } catch (e: Exception) {
        Napier.e("saveHighScore", e, "Error saving high score")
    }
}

private fun getAppSaveFilePath(): Path {
    val osName = System.getProperty("os.name").lowercase(Locale.getDefault())

    val appPath = if (osName.contains("win")) {
        val appData = System.getenv("APPDATA")
        "$appData\\CircleStacker".toPath()
    } else if (osName.contains("mac")) {
        val userHome = System.getProperty("user.home").toPath()
        userHome.resolve("Library/Application Support/CircleStacker")
    } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
        getLinuxPath()
    } else {
        // unknown OS do the same thing as for linux
        Napier.e { "Unknown OS: $osName" }
        getLinuxPath()
    }

    // make sure all paths are available
    FileSystem.SYSTEM.createDirectories(appPath)

    return appPath.resolve(HIGH_SCORE_FILE)
}

private fun getLinuxPath(): Path {
    var xdgDataHome = System.getenv("XDG_DATA_HOME")
    if (xdgDataHome == null || xdgDataHome.isEmpty()) {
        xdgDataHome = System.getProperty("user.home") + "/.local/share"
    }
    return xdgDataHome.toPath().resolve("CircleStacker")
}