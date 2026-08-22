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

    init {
        loadInstalledApps()
    }

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
                val appList = mutableListOf<InstalledApp>()
                
                val currentGames = gameDao.getAllGamesSync()
                val currentPackages = currentGames.map { it.packageName }.toSet()

                for (appInfo in packages) {
                    if (pm.getLaunchIntentForPackage(appInfo.packageName) != null) {
                        val name = pm.getApplicationLabel(appInfo).toString()
                        val packageName = appInfo.packageName
                        
                        appList.add(InstalledApp(name, packageName, appInfo))
                        
                        // Auto-detect and add games
                        val isGame = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            appInfo.category == ApplicationInfo.CATEGORY_GAME
                        } else {
                            (appInfo.flags and ApplicationInfo.FLAG_IS_GAME) != 0
                        }
                        
                        if (isGame && !currentPackages.contains(packageName)) {
                            gameDao.insertGame(Game(packageName = packageName, name = name))
                        }
                    }
                }
                
                _installedApps.value = appList.sortedBy { it.name }
            } catch (e: Throwable) { 
                e.printStackTrace()
            }
        }
    }
}
