package com.ultra.game.space

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ultra.game.space.ui.theme.MyApplicationTheme
import com.ultra.game.space.ui.navigation.AppNavigation
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.os.Build
import java.io.PrintWriter
import java.io.StringWriter

class MainActivity : ComponentActivity() {
  @android.annotation.SuppressLint("InvalidFragmentVersionForActivityResult")
  private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { _ -> }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
    Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
        try {
            val sw = StringWriter()
            exception.printStackTrace(PrintWriter(sw))
            val intent = Intent(this, CrashActivity::class.java).apply {
                putExtra("EXTRA_CRASH_LOG", sw.toString())
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(10)
        } catch (e: Exception) {
            // Ignore
        }
        defaultHandler?.uncaughtException(thread, exception)
    }
    
    enableEdgeToEdge()
    
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)
    insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    insetsController.hide(WindowInsetsCompat.Type.systemBars())
    
    val permissionsToRequest = mutableListOf<String>()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
    }
    // KILL_BACKGROUND_PROCESSES and VIBRATE are normal install-time permissions, but we ensure the framework is aware.
    if (permissionsToRequest.isNotEmpty()) {
        requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
    }

    setContent {
      MyApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          AppNavigation()
        }
      }
    }
  }
}
