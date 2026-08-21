package com.ultra.game.space.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ultra.game.space.models.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games")
    fun getAllGames(): Flow<List<Game>>

    @Insert
    suspend fun insertGame(game: Game)
    
    @Delete
    suspend fun deleteGame(game: Game)
}
