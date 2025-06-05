package cash.andrew.circlestacker

import android.app.Application
import android.content.Context
import java.lang.ref.WeakReference

class CircleStackerApp: Application() {
    override fun onCreate() {
        super.onCreate()

        _context = WeakReference(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        _context = null
    }

    companion object {
        private var _context: WeakReference<Context>? = null

        val context: Context get() = checkNotNull(_context?.get())
    }
}