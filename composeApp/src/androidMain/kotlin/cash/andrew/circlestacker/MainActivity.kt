package cash.andrew.circlestacker

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // lock orientation to what the user currently has device in
        // so it doesn't move during gameplay
        val orientation = resources.configuration.orientation
        requestedOrientation = when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            Configuration.ORIENTATION_PORTRAIT -> {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            else -> {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}