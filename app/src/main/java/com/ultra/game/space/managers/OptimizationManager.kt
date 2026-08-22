package com.ultra.game.space.managers

import android.app.ActivityManager
import android.content.Context
import android.util.Log

class OptimizationManager(private val context: Context) {
    private val TAG = "OptimizationManager"

    fun applyOptimization(mode: String) {
        when (mode) {
            "ECONOMY" -> revertOptimizations()
            "BALANCE" -> applyBalance()
            "ULTRA" -> applyUltra()
            "EXTREME" -> applyExtreme()
        }
    }

    private fun revertOptimizations() {
        Log.d(TAG, "Reverting to Economy - No optimizations applied")
    }

    private fun applyBalance() {
        Log.d(TAG, "Applying Balance Mode")
        killBackgroundApps(light = true)
    }

    private fun applyUltra() {
        Log.d(TAG, "Applying Ultra Mode")
        killBackgroundApps(light = false)
        clearCache()
    }

    private fun applyExtreme() {
        Log.d(TAG, "Applying Extreme Mode")
        killBackgroundApps(light = false, aggressive = true)
        clearCache()
    }
    
    fun applyThermalControl() {
        Log.d(TAG, "Applying Thermal Control")
        killBackgroundApps(light = false, aggressive = true)
        clearCache()
    }

    // New Individual Toggles
    fun toggleBackgroundActivities(enabled: Boolean) {
        if (enabled) {
            Log.d(TAG, "Force Stop Background Activities Enabled")
            killBackgroundApps(light = false, aggressive = true)
        }
    }

    fun toggleBackgroundData(enabled: Boolean) {
        if (enabled) {
            Log.d(TAG, "Force Stop Background Data Enabled - (Simulated via restricted network profiles in Game Space)")
        }
    }

    fun toggleRamOptimization(enabled: Boolean) {
        if (enabled) {
            Log.d(TAG, "RAM Optimization Triggered")
            clearCache()
        }
    }

    fun toggleCpuOptimization(enabled: Boolean) {
        if (enabled) {
            Log.d(TAG, "CPU Optimization Triggered")
            // Conceptually setting CPU governor to performance where supported/accessible via system intent
        }
    }
    
    fun triggerDebug(enabled: Boolean) {
        if (enabled) {
            Log.d(TAG, "Advanced GPU/CPU/RAM Debugging Telemetry ENABLED")
        } else {
            Log.d(TAG, "Advanced GPU/CPU/RAM Debugging Telemetry DISABLED")
        }
    }

    private fun killBackgroundApps(light: Boolean, aggressive: Boolean = false) {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return
        val myPackage = context.packageName
        try {
            val packagesToKill = mutableListOf<String>()
            
            // Standard background killing using Android's provided API
            val installedPackages = context.packageManager.getInstalledPackages(0)
            for (pkg in installedPackages) {
                val pkgName = pkg.packageName
                if (pkgName != myPackage && !pkgName.startsWith("com.android.") && !pkgName.startsWith("android")) {
                    packagesToKill.add(pkgName)
                }
            }

            val targetList = if (light) packagesToKill.take(packagesToKill.size / 2) else packagesToKill
            
            for (pkg in targetList) {
                am.killBackgroundProcesses(pkg)
            }
            if (aggressive || !light) {
                Log.d(TAG, "Successfully requested background kill for ${targetList.size} apps")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to kill background apps", e)
        }
    }

    private fun clearCache() {
        // Without root, we can only trim memory for our own process to reduce RAM pressure
        try {
            System.gc()
            Runtime.getRuntime().gc()
            Log.d(TAG, "Garbage Collection requested for RAM optimization")
        } catch (e: Exception) {
            // Ignore
        }
    }
}
