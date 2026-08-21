package com.ultra.game.space.viewmodels

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ultra.game.space.data.AppDatabase
import com.ultra.game.space.models.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class InstalledApp(
    val name: String,
    val packageName: String,
    val applicationInfo: ApplicationInfo
)

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val gameDao = AppDatabase.getDatabase(application, viewModelScope).gameDao()

    val games: StateFlow<List<Game>> = gameDao.getAllGames()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _installedApps = MutableStateFlow<List<InstalledApp>>(emptyList())
    val installedApps: StateFlow<List<InstalledApp>> = _installedApps

    fun addGame(game: Game) {
        viewModelScope.launch {
            gameDao.insertGame(game)
        }
    }

    fun removeGame(game: Game) {
        viewModelScope.launch {
            gameDao.deleteGame(game)
        }
    }

    fun loadInstalledApps() {
        viewModelScope.launch(Dispatchers.IO) {
            val pm = getApplication<Application>().packageManager
            try {
                val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
                val appList = packages.mapNotNull { appInfo ->
                    if (pm.getLaunchIntentForPackage(appInfo.packageName) != null) {
                        InstalledApp(
                            name = pm.getApplicationLabel(appInfo).toString(),
                            packageName = appInfo.packageName,
                            applicationInfo = appInfo
                        )
                    } else null
                }.sortedBy { it.name }
                _installedApps.value = appList
            } catch (e: Throwable) { 
                e.printStackTrace()
            }
        }
    }
}
