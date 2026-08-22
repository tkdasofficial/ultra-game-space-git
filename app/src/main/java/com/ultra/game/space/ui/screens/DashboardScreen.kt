package com.ultra.game.space.ui.screens

import android.content.pm.PackageManager
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.ultra.game.space.R
import com.ultra.game.space.models.Game
import com.ultra.game.space.models.Stat
import com.ultra.game.space.ui.components.*
import com.ultra.game.space.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Top-level extension functions for CQH (Container Query Height) equivalent scaling
private fun Double.cqh(multiplier: Float): Dp = (this.toFloat() * multiplier).dp
private fun Double.cqhSp(multiplier: Float): TextUnit = (this.toFloat() * multiplier).sp

@Composable
fun AppIconImage(
    packageName: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    alpha: Float = 1f
) {
    val context = LocalContext.current
    val pm = context.packageManager
    var appIcon by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    LaunchedEffect(packageName) {
        withContext(Dispatchers.IO) {
            try {
                val drawable = pm.getApplicationIcon(packageName)
                appIcon = drawable.toBitmap(width = 150, height = 150)
            } catch (e: Throwable) {
                // Ignore
            }
        }
    }

    if (appIcon != null) {
        Image(
            bitmap = appIcon!!.asImageBitmap(),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop,
            alpha = alpha
        )
    } else {
        Box(modifier = modifier.background(PanelDark2), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.VideogameAsset, contentDescription = contentDescription, tint = TextSecondary)
        }
    }
}

@Composable
fun DashboardScreen(
    gameViewModel: com.ultra.game.space.viewmodels.GameViewModel,
    statsViewModel: com.ultra.game.space.viewmodels.SystemStatsViewModel,
    onNavigateToLobby: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAppManagement: () -> Unit
) {
    var activeGameIndex by remember { mutableIntStateOf(0) }
    var performanceMode by remember { mutableStateOf("ECONOMY") }
    var isModeMenuOpen by remember { mutableStateOf(false) }
    var menuOffset by remember { mutableStateOf(androidx.compose.ui.unit.DpOffset.Zero) }
    val density = androidx.compose.ui.platform.LocalDensity.current

    val games by gameViewModel.games.collectAsState()
    val stats by statsViewModel.stats.collectAsState()
    
    val context = androidx.compose.ui.platform.LocalContext.current
    val optManager = remember { com.ultra.game.space.managers.OptimizationManager(context) }
    
    LaunchedEffect(performanceMode) {
        optManager.applyOptimization(performanceMode)
    }
    
    // Ensure active index is valid
    if (games.isNotEmpty() && activeGameIndex >= games.size) {
        activeGameIndex = 0
    }

    val modes = listOf("ECONOMY", "BALANCE", "ULTRA", "EXTREME")
    val isBoosted = performanceMode != "ECONOMY"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        val configuration = androidx.compose.ui.platform.LocalConfiguration.current
        val m = configuration.screenHeightDp / 100f

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
                    Text(
                        text = "ULTRA GAME SPACE",
                        color = TextPrimary,
                        fontSize = 3.1.cqhSp(m),
                        fontWeight = FontWeight.Bold,
                        fontFamily = Rajdhani,
                        letterSpacing = 0.06.sp
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(7.6.cqh(m))
                        .border(1.dp, BorderDark, ClipNotchShape(15.dp))
                        .background(PanelDark2, ClipNotchShape(15.dp))
                        .clip(ClipNotchShape(15.dp))
                        .clickable(onClick = onNavigateToSettings),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = TextPrimary,
                        modifier = Modifier.size(5.2.cqh(m))
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.4.cqh(m)))

            // Body
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Stats Panel (34% width)
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.34f)
                        .border(1.dp, BorderDark, ClipNotchShape(15.dp))
                        .background(PanelDark, ClipNotchShape(15.dp))
                        .padding(horizontal = 2.2.cqh(m), vertical = 2.0.cqh(m)),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    stats.forEach { stat ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.0.cqh(m))
                                    .border(1.dp, PrimaryRed.copy(alpha = 0.6f), ClipNotchShape(8.dp))
                                    .background(PanelDark2, ClipNotchShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    stat.icon,
                                    contentDescription = stat.label,
                                    tint = PrimaryRed,
                                    modifier = Modifier.size(3.4.cqh(m))
                                )
                            }
                            Spacer(modifier = Modifier.width(1.8.cqh(m)))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = stat.label, 
                                        color = TextPrimary, 
                                        fontSize = 2.4.cqhSp(m), 
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = Rajdhani,
                                        letterSpacing = 0.08.sp
                                    )
                                    Text(
                                        text = stat.value, 
                                        color = TextPrimary.copy(alpha = 0.9f), 
                                        fontSize = 2.3.cqhSp(m), 
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = Rajdhani
                                    )
                                }
                                Spacer(modifier = Modifier.height(0.9.cqh(m)))
                                // Progress bar
                                val animatedProgress by androidx.compose.animation.core.animateFloatAsState(
                                    targetValue = stat.pct / 100f,
                                    animationSpec = androidx.compose.animation.core.tween(durationMillis = 800)
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(0.9.cqh(m))
                                        .background(TrackDark, CircleShape)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(animatedProgress)
                                            .fillMaxHeight()
                                            .background(
                                                Brush.horizontalGradient(listOf(PrimaryRedLight, PrimaryRedDark)),
                                                CircleShape
                                            )
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(3.0.cqh(m)))

                // Carousel
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (games.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(26.0.cqh(m))
                                .border(2.dp, BorderDark, CircleShape)
                                .background(PanelDark.copy(alpha = 0.6f), CircleShape)
                                .clip(CircleShape)
                                .clickable(onClick = onNavigateToAppManagement),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add Game",
                                tint = PrimaryRed,
                                modifier = Modifier.size(8.0.cqh(m))
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = { activeGameIndex = (activeGameIndex + games.size - 1) % games.size }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack, 
                                    contentDescription = "Previous", 
                                    tint = TextSecondary, 
                                    modifier = Modifier.size(3.4.cqh(m))
                                )
                            }
                            
                            // Games list display
                            val prev = (activeGameIndex + games.size - 1) % games.size
                            val next = (activeGameIndex + 1) % games.size
                            
                            val indicesToShow = if (games.size >= 3) {
                                listOf(prev, activeGameIndex, next)
                            } else if (games.size == 2) {
                                listOf(prev, activeGameIndex)
                            } else {
                                listOf(activeGameIndex)
                            }
                            
                            indicesToShow.forEachIndexed { pos, i ->
                                val isActive = (games.size >= 3 && pos == 1) || (games.size < 3 && i == activeGameIndex)
                                val g = games[i]
                                val size = if (isActive) 26.0.cqh(m) else 18.0.cqh(m)
                                
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable { activeGameIndex = i }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(size)
                                            .let {
                                                if (isActive) {
                                                    it.border(2.dp, PrimaryRed, CircleShape)
                                                      .padding(1.4.cqh(m))
                                                } else {
                                                    it.padding(0.9.cqh(m))
                                                }
                                            }
                                    ) {
                                        AppIconImage(
                                            packageName = g.packageName,
                                            contentDescription = g.name,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape),
                                            alpha = if (isActive) 1f else 0.65f
                                        )
                                    }
                                    if (!isActive) {
                                        Spacer(modifier = Modifier.height(1.0.cqh(m)))
                                        Text(
                                            text = g.name, 
                                            color = TextSecondary, 
                                            fontSize = 1.9.cqhSp(m), 
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = Rajdhani
                                        )
                                    }
                                }
                            }
                            
                            IconButton(onClick = { activeGameIndex = (activeGameIndex + 1) % games.size }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward, 
                                    contentDescription = "Next", 
                                    tint = TextSecondary, 
                                    modifier = Modifier.size(3.4.cqh(m))
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(2.4.cqh(m)))
                        
                        Text(
                            text = games[activeGameIndex].name.uppercase(),
                            color = TextPrimary,
                            fontSize = 3.6.cqhSp(m),
                            fontWeight = FontWeight.Bold,
                            fontFamily = Rajdhani,
                            letterSpacing = 0.05.sp
                        )
                        
                        Spacer(modifier = Modifier.height(1.0.cqh(m)))
                        
                        Box(
                            modifier = Modifier
                                .width(9.0.cqh(m))
                                .height(0.5.cqh(m))
                                .background(PrimaryRed, CircleShape)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.4.cqh(m)))

            // Footer Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Performance Mode (30% width)
                Box(modifier = Modifier.weight(0.33f).height(8.0.cqh(m))) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = { offset ->
                                        menuOffset = androidx.compose.ui.unit.DpOffset(
                                            x = with(density) { offset.x.toDp() },
                                            y = with(density) { offset.y.toDp() }
                                        )
                                        isModeMenuOpen = true
                                    }
                                )
                            }
                            .background(if (isBoosted) PrimaryRed.copy(alpha=0.15f) else PanelDark, ClipTabLShape(15.dp))
                            .border(1.dp, if (isBoosted) PrimaryRed else BorderDark, ClipTabLShape(15.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Speed, 
                                contentDescription = null, 
                                tint = if (isBoosted) TextPrimary else PrimaryRed, 
                                modifier = Modifier.size(3.0.cqh(m))
                            )
                            Spacer(modifier = Modifier.width(1.6.cqh(m)))
                            Text(
                                text = if (isBoosted) performanceMode else "PERFORMANCE MODE", 
                                color = TextPrimary, 
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = Rajdhani,
                                fontSize = 2.0.cqhSp(m),
                                letterSpacing = 0.08.sp
                            )
                        }
                    }
                    
                    DropdownMenu(
                        expanded = isModeMenuOpen,
                        onDismissRequest = { isModeMenuOpen = false },
                        offset = menuOffset,
                        modifier = Modifier.background(PanelDark).border(1.dp, BorderDark)
                    ) {
                        modes.forEach { mStr ->
                            DropdownMenuItem(
                                text = { 
                                    Text(
                                        text = mStr, 
                                        color = if (mStr == performanceMode) PrimaryRed else TextSecondary, 
                                        fontWeight = FontWeight.Bold, 
                                        fontFamily = Rajdhani,
                                        fontSize = 2.0.cqhSp(m)
                                    ) 
                                },
                                onClick = {
                                    performanceMode = mStr
                                    isModeMenuOpen = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(2.4.cqh(m)))

                // Boost Now (flex-1)
                val scope = androidx.compose.runtime.rememberCoroutineScope()
                var boostText by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("BOOST NOW") }
                val context = androidx.compose.ui.platform.LocalContext.current
                
                Box(
                    modifier = Modifier
                        .weight(0.34f)
                        .height(9.4.cqh(m))
                        .clickable { 
                            scope.launch {
                                boostText = "BOOSTING..."
                                optManager.applyOptimization("EXTREME")
                                delay(1000)
                                Toast.makeText(context, "Memory Freed & Background Tasks Killed", Toast.LENGTH_SHORT).show()
                                boostText = "BOOSTED"
                                delay(2000)
                                boostText = "BOOST NOW"
                            }
                        }
                        .background(
                            Brush.horizontalGradient(listOf(PrimaryRedLight, PrimaryRedDark)), 
                            ClipHexShape(15.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = boostText, 
                        color = TextPrimary,
                        fontSize = 3.2.cqhSp(m), 
                        fontWeight = FontWeight.Bold, 
                        fontFamily = Rajdhani,
                        letterSpacing = 0.06.sp
                    )
                }

                Spacer(modifier = Modifier.width(2.4.cqh(m)))

                // Game Lobby (30% width)
                Box(
                    modifier = Modifier
                        .weight(0.33f)
                        .height(8.0.cqh(m))
                        .clickable { onNavigateToLobby() }
                        .background(PanelDark, ClipTabRShape(15.dp))
                        .border(1.dp, BorderDark, ClipTabRShape(15.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Games, 
                            contentDescription = null, 
                            tint = PrimaryRed, 
                            modifier = Modifier.size(3.0.cqh(m))
                        )
                        Spacer(modifier = Modifier.width(1.6.cqh(m)))
                        Text(
                            text = "GAME LOBBY", 
                            color = TextPrimary, 
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = Rajdhani,
                            fontSize = 2.0.cqhSp(m),
                            letterSpacing = 0.08.sp
                        )
                    }
                }
            }
        }
    }
}
