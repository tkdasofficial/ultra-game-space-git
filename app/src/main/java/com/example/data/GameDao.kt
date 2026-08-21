package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.models.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games")
    fun getAllGames(): Flow<List<Game>>

    @Insert
    suspend fun insertGame(game: Game)
}
