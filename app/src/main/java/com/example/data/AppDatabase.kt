package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.models.Game
import com.example.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Game::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.gameDao())
                }
            }
        }

        suspend fun populateDatabase(gameDao: GameDao) {
            gameDao.insertGame(Game(name = "CALL OF DUTY", imageResId = R.drawable.game_cod, mode = "ULTRA", hours = "42h 10m", fps = "120 FPS"))
            gameDao.insertGame(Game(name = "PUBG MOBILE", imageResId = R.drawable.game_pubg, mode = "EXTREME", hours = "78h 55m", fps = "90 FPS"))
            gameDao.insertGame(Game(name = "ASPHALT 9", imageResId = R.drawable.game_asphalt, mode = "BALANCE", hours = "12h 30m", fps = "60 FPS"))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "game_database"
                )
                .addCallback(AppDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
