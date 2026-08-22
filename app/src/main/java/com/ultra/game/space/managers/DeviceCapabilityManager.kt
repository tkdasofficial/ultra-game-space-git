package com.ultra.game.space.managers

import android.content.Context
import android.os.Build
import android.view.WindowManager
import android.app.ActivityManager
import android.content.pm.PackageManager

object DeviceCapabilityManager {

    data class DeviceProfile(
        val maxCpuFreq: Double, // GHz
        val maxGpuFreq: Double, // MHz
        val maxFps: Int,
        val maxResolution: String
    )

    fun getProfile(context: Context): DeviceProfile {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        am?.getMemoryInfo(memoryInfo)
        val totalRamGb = memoryInfo.totalMem.toDouble() / (1024 * 1024 * 1024)

        // Read supported display modes for FPS limits
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        var maxDisplayFps = 60
        var maxDisplayHeight = 1080
        
        try {
            val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try { context.display } catch (e: UnsupportedOperationException) { null } ?: wm?.defaultDisplay
            } else {
                wm?.defaultDisplay
            }
            
            if (display != null) {
                for (mode in display.supportedModes) {
                    if (mode.refreshRate > maxDisplayFps) {
                        maxDisplayFps = mode.refreshRate.toInt()
                    }
                    if (mode.physicalHeight > maxDisplayHeight) {
                        maxDisplayHeight = mode.physicalHeight
                    }
                }
            }
        } catch (e: Exception) {
            // Fallback gracefully
        }

        val maxCpu = when {
            totalRamGb > 10.0 -> 3.20
            totalRamGb > 6.0 -> 2.84
            else -> 2.40
        }

        val maxGpu = when {
            totalRamGb > 10.0 -> 2100.0
            totalRamGb > 6.0 -> 1500.0
            else -> 1114.0
        }

        val maxRes = when {
            maxDisplayHeight >= 2160 -> "4K"
            maxDisplayHeight >= 1440 -> "2K"
            maxDisplayHeight >= 1080 -> "1080P"
            else -> "720P"
        }

        return DeviceProfile(maxCpu, maxGpu, maxDisplayFps, maxRes)
    }
}
