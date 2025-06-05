package cash.andrew.circlestacker

import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import circle_stacker.composeapp.generated.resources.Res
import circle_stacker.composeapp.generated.resources.app_icon
import org.jetbrains.compose.resources.painterResource
import java.awt.Taskbar

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Circle Stacker",
        icon = painterResource(Res.drawable.app_icon)
    ) {
        Taskbar.getTaskbar().iconImage = painterResource(Res.drawable.app_icon).toAwtImage(
            density = Density(20f),
            layoutDirection = LayoutDirection.Ltr
        )

        App()
    }
}