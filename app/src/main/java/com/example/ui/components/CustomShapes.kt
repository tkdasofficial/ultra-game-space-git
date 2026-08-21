package com.example.ui.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class ClipNotchShape(private val cut: Dp) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val cutPx = with(density) { cut.toPx() }
        val path = Path().apply {
            moveTo(cutPx, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height - cutPx)
            lineTo(size.width - cutPx, size.height)
            lineTo(0f, size.height)
            lineTo(0f, cutPx)
            close()
        }
        return Outline.Generic(path)
    }
}

class ClipTabLShape(private val cut: Dp) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val cutPx = with(density) { cut.toPx() }
        val path = Path().apply {
            moveTo(cutPx, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            lineTo(0f, cutPx)
            close()
        }
        return Outline.Generic(path)
    }
}

class ClipTabRShape(private val cut: Dp) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val cutPx = with(density) { cut.toPx() }
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height - cutPx)
            lineTo(size.width - cutPx, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}

class ClipHexShape(private val cut: Dp) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val cutPx = with(density) { cut.toPx() }
        val hHalf = size.height / 2f
        val path = Path().apply {
            moveTo(cutPx, 0f)
            lineTo(size.width - cutPx, 0f)
            lineTo(size.width, hHalf)
            lineTo(size.width - cutPx, size.height)
            lineTo(cutPx, size.height)
            lineTo(0f, hHalf)
            close()
        }
        return Outline.Generic(path)
    }
}
