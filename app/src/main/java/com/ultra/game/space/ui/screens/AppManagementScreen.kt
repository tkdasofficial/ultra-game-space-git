package com.ultra.game.space.ui.screens

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.ultra.game.space.models.Game
import com.ultra.game.space.ui.components.ClipNotchShape
import com.ultra.game.space.ui.theme.*
import com.ultra.game.space.viewmodels.GameViewModel
import com.ultra.game.space.viewmodels.InstalledApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private fun Double.cqh(multiplier: Float): Dp = (this.toFloat() * multiplier).dp
private fun Double.cqhSp(multiplier: Float): TextUnit = (this.toFloat() * multiplier).sp

@Composable
fun AppManagementScreen(gameViewModel: GameViewModel, onNavigateBack: () -> Unit) {
    val configuration = LocalConfiguration.current
    val m = configuration.screenHeightDp / 100f
    
    val installedApps by gameViewModel.installedApps.collectAsState()
    val games by gameViewModel.games.collectAsState()

    LaunchedEffect(Unit) {
        gameViewModel.loadInstalledApps()
    }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(PrimaryRed.copy(alpha = 0.12f), Color.Transparent),
                        radius = configuration.screenHeightDp.toFloat() * 2f
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 3.5.cqh(m), vertical = 2.6.cqh(m))
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(3.0.cqh(m))
                            .background(PrimaryRed, ClipNotchShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(1.6.cqh(m)))
                    Text(
                        text = "APP MANAGEMENT",
                        color = TextPrimary,
                        fontSize = 2.9.cqhSp(m),
                        fontWeight = FontWeight.Bold,
                        fontFamily = Rajdhani,
                        letterSpacing = 0.12.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .size(7.6.cqh(m))
                        .border(1.dp, BorderDark, ClipNotchShape(14.dp))
                        .background(PanelDark2, ClipNotchShape(14.dp))
                        .clip(ClipNotchShape(14.dp))
                        .clickable(onClick = onNavigateBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Close, 
                        contentDescription = "Close", 
                        tint = TextPrimary, 
                        modifier = Modifier.size(5.2.cqh(m))
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(2.4.cqh(m)))

            if (installedApps.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryRed)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(1.4.cqh(m))
                ) {
                    items(installedApps) { app ->
                        val isAdded = games.any { it.packageName == app.packageName }
                        val addedGame = games.find { it.packageName == app.packageName }
                        
                        AppManagementItem(
                            app = app,
                            isAdded = isAdded,
                            m = m,
                            onToggle = {
                                if (isAdded && addedGame != null) {
                                    gameViewModel.removeGame(addedGame)
                                } else {
                                    gameViewModel.addGame(
                                        Game(
                                            name = app.name,
                                            packageName = app.packageName
                                        )
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppManagementItem(
    app: InstalledApp,
    isAdded: Boolean,
    m: Float,
    onToggle: () -> Unit
) {
    val context = LocalContext.current
    val pm = context.packageManager
    var appIcon by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    LaunchedEffect(app.packageName) {
        withContext(Dispatchers.IO) {
            try {
                val drawable = pm.getApplicationIcon(app.packageName)
                appIcon = drawable.toBitmap(width = 150, height = 150)
            } catch (e: Throwable) {
                // Ignore icon loading errors
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, if (isAdded) PrimaryRed else BorderDark, ClipNotchShape(14.dp))
            .background(if (isAdded) PrimaryRed.copy(alpha = 0.05f) else PanelDark, ClipNotchShape(14.dp))
            .clip(ClipNotchShape(14.dp))
            .clickable { onToggle() }
            .padding(1.6.cqh(m)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(6.0.cqh(m))
                    .clip(CircleShape)
                    .background(PanelDark2),
                contentAlignment = Alignment.Center
            ) {
                appIcon?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = app.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.width(2.0.cqh(m)))
            Column {
                Text(
                    text = app.name,
                    color = TextPrimary,
                    fontSize = 2.2.cqhSp(m),
                    fontWeight = FontWeight.Bold,
                    fontFamily = Rajdhani,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = app.packageName,
                    color = TextSecondary,
                    fontSize = 1.6.cqhSp(m),
                    fontFamily = Rajdhani,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(2.0.cqh(m)))

        Switch(
            checked = isAdded,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = TextPrimary,
                checkedTrackColor = PrimaryRed,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = PanelDark2
            )
        )
    }
}
