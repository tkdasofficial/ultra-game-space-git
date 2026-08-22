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
        var maxDisplayFps = 0
        var maxDisplayHeight = 0
        
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
                
                // Fallback to current mode if supported modes iteration didn't yield anything
                if (maxDisplayHeight == 0) {
                    maxDisplayHeight = display.mode.physicalHeight
                }
                if (maxDisplayFps == 0) {
                    maxDisplayFps = display.mode.refreshRate.toInt()
                }
            }
        } catch (e: Exception) {
            // Fallback gracefully
        }

        // Failsafe defaults if completely unable to read (e.g. headless emulator)
        if (maxDisplayHeight == 0) maxDisplayHeight = 720
        if (maxDisplayFps == 0) maxDisplayFps = 60

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

        // Cap based on actual physical height, ensuring we accurately report HD limits
        val maxRes = when {
            maxDisplayHeight >= 2160 -> "4K"
            maxDisplayHeight >= 1440 -> "2K"
            maxDisplayHeight >= 1080 -> "1080P"
            else -> "HD"
        }

        return DeviceProfile(maxCpu, maxGpu, maxDisplayFps, maxRes)
    }
}
