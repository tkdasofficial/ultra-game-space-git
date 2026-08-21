package com.example.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.models.Game
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val gameDao = AppDatabase.getDatabase(application, viewModelScope).gameDao()

    val games: StateFlow<List<Game>> = gameDao.getAllGames()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addGame(game: Game) {
        viewModelScope.launch {
            gameDao.insertGame(game)
        }
    }
}
