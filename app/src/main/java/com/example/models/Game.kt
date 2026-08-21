package com.example.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val packageName: String,
    val mode: String = "ULTRA",
    val hours: String = "0h 0m",
    val fps: String = "60 FPS"
)
