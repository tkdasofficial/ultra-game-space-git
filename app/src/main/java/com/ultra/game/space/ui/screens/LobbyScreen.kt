package com.ultra.game.space.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ultra.game.space.R
import com.ultra.game.space.models.Game
import com.ultra.game.space.ui.components.ClipNotchShape
import com.ultra.game.space.ui.components.ClipTabLShape
import com.ultra.game.space.ui.theme.*

private fun Double.cqh(multiplier: Float): Dp = (this.toFloat() * multiplier).dp
private fun Double.cqhSp(multiplier: Float): TextUnit = (this.toFloat() * multiplier).sp

@Composable
fun LobbyScreen(
    gameViewModel: com.ultra.game.space.viewmodels.GameViewModel, 
    onNavigateBack: () -> Unit,
    onNavigateToAppManagement: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val m = configuration.screenHeightDp / 100f
    
    val library by gameViewModel.games.collectAsState()
    
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
                        text = "GAME LOBBY",
                        color = TextPrimary,
                        fontSize = 2.9.cqhSp(m),
                        fontWeight = FontWeight.Bold,
                        fontFamily = Rajdhani,
                        letterSpacing = 0.12.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .size(5.0.cqh(m))
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
                        modifier = Modifier.size(2.6.cqh(m))
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(2.4.cqh(m)))
            
            // Grid
            BoxWithConstraints(modifier = Modifier.weight(1f).fillMaxWidth()) {
                val gridHeight = this.maxHeight
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(2.4.cqh(m)),
                    verticalArrangement = Arrangement.spacedBy(2.4.cqh(m)),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(library) { game ->
                        GameCard(game, m, Modifier.height(gridHeight))
                    }
                    item {
                        // Add Game Card
                        Box(
                            modifier = Modifier
                                .height(gridHeight)
                                .border(1.dp, BorderDark, ClipNotchShape(14.dp))
                                .background(PanelDark.copy(alpha = 0.6f), ClipNotchShape(14.dp))
                                .clip(ClipNotchShape(14.dp))
                                .clickable { onNavigateToAppManagement() },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.Add, 
                                    contentDescription = "Add", 
                                    tint = TextSecondary, 
                                    modifier = Modifier.size(4.0.cqh(m))
                                )
                                Spacer(modifier = Modifier.height(1.4.cqh(m)))
                                Text(
                                    text = "ADD GAME", 
                                    color = TextSecondary, 
                                    fontSize = 2.0.cqhSp(m), 
                                    fontWeight = FontWeight.SemiBold, 
                                    fontFamily = Rajdhani,
                                    letterSpacing = 0.1.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameCard(game: Game, m: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .border(1.dp, BorderDark, ClipNotchShape(14.dp))
            .background(PanelDark, ClipNotchShape(14.dp))
            .padding(1.6.cqh(m))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AppIconImage(
                    packageName = game.packageName,
                    contentDescription = game.name,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = game.mode,
                    color = TextPrimary,
                    fontSize = 1.6.cqhSp(m),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Rajdhani,
                    letterSpacing = 0.08.sp,
                    modifier = Modifier
                        .background(PrimaryRed)
                        .padding(horizontal = 1.2.cqh(m), vertical = 0.4.cqh(m))
                )
            }
            
            Spacer(modifier = Modifier.height(1.4.cqh(m)))
            
            Text(
                text = game.name,
                color = TextPrimary,
                fontSize = 2.2.cqhSp(m),
                fontWeight = FontWeight.Bold,
                fontFamily = Rajdhani,
                letterSpacing = 0.06.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(0.8.cqh(m)))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Schedule, 
                        contentDescription = null, 
                        tint = TextSecondary, 
                        modifier = Modifier.size(1.8.cqh(m))
                    )
                    Spacer(modifier = Modifier.width(0.6.cqh(m)))
                    Text(
                        text = game.hours, 
                        color = TextSecondary, 
                        fontSize = 1.7.cqhSp(m),
                        fontFamily = Rajdhani
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Speed, 
                        contentDescription = null, 
                        tint = PrimaryRed, 
                        modifier = Modifier.size(1.8.cqh(m))
                    )
                    Spacer(modifier = Modifier.width(0.6.cqh(m)))
                    Text(
                        text = game.fps, 
                        color = TextSecondary, 
                        fontSize = 1.7.cqhSp(m),
                        fontFamily = Rajdhani
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(1.4.cqh(m)))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.4.cqh(m))
                    .background(PrimaryRed, ClipTabLShape(18.dp))
                    .clip(ClipTabLShape(18.dp))
                    .clickable { /* Launch */ },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.PlayArrow, 
                        contentDescription = null, 
                        tint = TextPrimary,
                        modifier = Modifier.size(2.2.cqh(m))
                    )
                    Spacer(modifier = Modifier.width(1.0.cqh(m)))
                    Text(
                        text = "LAUNCH", 
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold, 
                        fontSize = 2.0.cqhSp(m),
                        fontFamily = Rajdhani,
                        letterSpacing = 0.08.sp
                    )
                }
            }
        }
    }
}
