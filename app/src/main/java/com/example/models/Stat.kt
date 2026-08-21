package com.example.models

import androidx.compose.ui.graphics.vector.ImageVector

data class Stat(
    val label: String,
    val value: String,
    val pct: Int,
    val icon: ImageVector
)
