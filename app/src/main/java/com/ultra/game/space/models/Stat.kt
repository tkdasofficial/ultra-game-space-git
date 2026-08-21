package com.ultra.game.space.models

import androidx.compose.ui.graphics.vector.ImageVector

data class Stat(
    val label: String,
    val value: String,
    val pct: Int,
    val icon: ImageVector
)
