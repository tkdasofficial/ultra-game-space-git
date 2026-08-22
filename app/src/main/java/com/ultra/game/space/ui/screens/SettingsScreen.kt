package com.ultra.game.space.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.drawBehind
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ultra.game.space.ui.components.*
import com.ultra.game.space.ui.theme.*

private fun Double.cqh(multiplier: Float): Dp = (this.toFloat() * multiplier).dp
private fun Double.cqhSp(multiplier: Float): TextUnit = (this.toFloat() * multiplier).sp

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    var selectedTab by remember { mutableStateOf("DISPLAY") }
    val nav = listOf("DISPLAY", "NOTIFICATION", "VIBRATION", "SOUND", "RECORDING", "SYSTEM")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        val configuration = androidx.compose.ui.platform.LocalConfiguration.current
        val m = configuration.screenHeightDp / 100f

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Sidebar
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.2f)
                    .background(PanelDark)
                    .padding(vertical = 3.0.cqh(m))
            ) {
                nav.forEach { item ->
                    val isSelected = selectedTab == item
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedTab = item }
                            .let {
                                if (isSelected) {
                                    it.background(PrimaryRed, ClipTabLShape(15.dp))
                                } else {
                                    it
                                }
                            }
                            .padding(horizontal = 3.0.cqh(m), vertical = 2.0.cqh(m))
                    ) {
                        Text(
                            text = item,
                            color = if (isSelected) TextPrimary else TextSecondary,
                            fontSize = 2.2.cqhSp(m),
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = Rajdhani,
                            letterSpacing = 0.08.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = "v1.0.0-beta",
                    color = TextSecondary,
                    fontSize = 1.8.cqhSp(m),
                    modifier = Modifier.padding(horizontal = 3.0.cqh(m)),
                    fontFamily = Rajdhani
                )
            }

            // Main Content Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 3.0.cqh(m), vertical = 2.4.cqh(m))
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SETTINGS",
                        color = TextPrimary,
                        fontSize = 2.8.cqhSp(m),
                        fontWeight = FontWeight.Bold,
                        fontFamily = Rajdhani,
                        letterSpacing = 0.14.sp
                    )
                    Spacer(modifier = Modifier.width(2.0.cqh(m)))
                    Box(
                        modifier = Modifier
                            .size(7.6.cqh(m))
                            .border(1.dp, BorderDark, ClipNotchShape(15.dp))
                            .background(PanelDark2, ClipNotchShape(15.dp))
                            .clip(ClipNotchShape(15.dp))
                            .clickable(onClick = onNavigateBack),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ChevronLeft,
                            contentDescription = "Back",
                            tint = TextPrimary,
                            modifier = Modifier.size(5.2.cqh(m))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(1.6.cqh(m)))

                // Scrollable Settings Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(end = 1.0.cqh(m))
                ) {
                    when (selectedTab) {
                        "DISPLAY" -> DisplaySettings(m)
                        "NOTIFICATION" -> NotificationSettings(m)
                        "VIBRATION" -> VibrationSettings(m)
                        "SOUND" -> SoundSettings(m)
                        "RECORDING" -> RecordingSettings(m)
                        "SYSTEM" -> SystemSettings(m)
                    }
                }
            }
        }
    }
}

// --- Specific Setting Screens ---

@Composable
fun DisplaySettings(m: Float) {
    var graphics by remember { mutableStateOf("HD") }
    var frameRate by remember { mutableStateOf("60") }
    var hdrColorBoost by remember { mutableStateOf(false) }
    var antiAliasing by remember { mutableStateOf(true) }
    var autoTouch by remember { mutableStateOf(false) }

    SettingsSectionTitle("Display", m)
    SettingsRow("Graphics", hint = true, m = m) {
        SettingsSegmentedControl(listOf("SMOOTH", "STANDARD", "HD", "FHD", "HDR", "ULTRA HDR", "EXTREME"), graphics, m) { graphics = it }
    }
    SettingsRow("HDR Color Boost", sub = true, m = m) {
        SettingsToggle(hdrColorBoost, m) { hdrColorBoost = it }
    }

    SettingsSectionTitle("Frame Rate", m)
    SettingsRow("FPS Settings", hint = true, m = m) {
        SettingsSegmentedControl(listOf("AUTO", "60", "90", "120", "EXTREME"), frameRate, m) { frameRate = it }
    }
    SettingsRow("Anti-Aliasing", sub = true, m = m) {
        SettingsToggle(antiAliasing, m) { antiAliasing = it }
    }

    SettingsSectionTitle("Developer", m)
    SettingsRow("Auto Touch Debugging", hint = true, m = m) {
        SettingsToggle(autoTouch, m) { autoTouch = it }
    }
}

@Composable
fun NotificationSettings(m: Float) {
    var boostAlerts by remember { mutableStateOf(true) }
    var overheatWarnings by remember { mutableStateOf(true) }
    var gameInvites by remember { mutableStateOf(false) }
    var dnd by remember { mutableStateOf(false) }

    SettingsSectionTitle("Alerts", m)
    SettingsRow("Boost Alerts", m = m) { SettingsToggle(boostAlerts, m) { boostAlerts = it } }
    SettingsRow("Overheat Warnings", hint = true, m = m) { SettingsToggle(overheatWarnings, m) { overheatWarnings = it } }
    
    SettingsSectionTitle("Social", m)
    SettingsRow("Game Invites", m = m) { SettingsToggle(gameInvites, m) { gameInvites = it } }
    
    SettingsSectionTitle("Focus", m)
    SettingsRow("Do-Not-Disturb in Game", sub = true, m = m) { SettingsToggle(dnd, m) { dnd = it } }
}

@Composable
fun RecordingSettings(m: Float) {
    var resolution by remember { mutableStateOf("1080P") }
    var frameRate by remember { mutableStateOf("60FPS") }
    var bitrate by remember { mutableStateOf(12f) }
    var audioSource by remember { mutableStateOf("INTERNAL") }

    SettingsSectionTitle("Video", m)
    SettingsRow("Resolution", m = m) {
        SettingsSegmentedControl(listOf("480P", "720P", "1080P", "2K", "4K"), resolution, m) { resolution = it }
    }
    SettingsRow("Frame Rate", m = m) {
        SettingsSegmentedControl(listOf("24FPS", "30FPS", "60FPS", "120FPS"), frameRate, m) { frameRate = it }
    }
    SettingsSlider("Bitrate", bitrate, 1f, 64f, 1f, " Mbps", m) { bitrate = it }

    SettingsSectionTitle("Audio", m)
    SettingsRow("Audio Source", m = m) {
        SettingsSegmentedControl(listOf("MUTE", "INTERNAL", "MIC", "DUAL"), audioSource, m) { audioSource = it }
    }
}

@Composable
fun SoundSettings(m: Float) {
    var tapSound by remember { mutableStateOf(true) }
    var startSound by remember { mutableStateOf(true) }
    var appSound by remember { mutableStateOf(false) }
    var volume by remember { mutableStateOf(80f) }
    var audioOutput by remember { mutableStateOf("SPEAKER") }

    SettingsSectionTitle("UI Sounds", m)
    SettingsRow("Tap Sound", m = m) { SettingsToggle(tapSound, m) { tapSound = it } }
    SettingsRow("Game Start Sound", m = m) { SettingsToggle(startSound, m) { startSound = it } }
    SettingsRow("App Opening Sound", sub = true, m = m) { SettingsToggle(appSound, m) { appSound = it } }

    SettingsSectionTitle("Master", m)
    SettingsSlider("Master Volume", volume, 0f, 100f, 5f, "%", m) { volume = it }
    SettingsRow("Audio Output Mode", hint = true, m = m) {
        SettingsSegmentedControl(listOf("SPEAKER", "HEADSET", "BLUETOOTH"), audioOutput, m) { audioOutput = it }
    }
}

@Composable
fun SystemSettings(m: Float) {
    var forceStop by remember { mutableStateOf(true) }
    var debugging by remember { mutableStateOf(false) }
    var forceFps by remember { mutableStateOf(false) }

    SettingsSectionTitle("Optimization", m)
    SettingsRow("Force Stop Background Activities", hint = true, m = m) { SettingsToggle(forceStop, m) { forceStop = it } }
    SettingsRow("Force FPS Lock", sub = true, m = m) { SettingsToggle(forceFps, m) { forceFps = it } }

    SettingsSectionTitle("Telemetry", m)
    SettingsRow("GPU/CPU/RAM Debugging", hint = true, m = m) { SettingsToggle(debugging, m) { debugging = it } }
}

@Composable
fun VibrationSettings(m: Float) {
    var master by remember { mutableStateOf(true) }
    var startVib by remember { mutableStateOf(true) }
    var tapVib by remember { mutableStateOf(false) }
    var intensity by remember { mutableStateOf(75f) }

    SettingsSectionTitle("Haptics", m)
    SettingsRow("Master Haptics", m = m) { SettingsToggle(master, m) { master = it } }
    SettingsRow("Game Start Vibration", m = m) { SettingsToggle(startVib, m) { startVib = it } }
    SettingsRow("Tap Feedback", sub = true, m = m) { SettingsToggle(tapVib, m) { tapVib = it } }

    SettingsSectionTitle("Strength", m)
    SettingsSlider("Vibration Intensity", intensity, 0f, 100f, 5f, "%", m) { intensity = it }
}

// --- Reusable Settings UI Components ---

@Composable
fun SettingsSectionTitle(title: String, m: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.2.cqh(m), bottom = 1.1.cqh(m)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .height(2.1.cqh(m))
            .width(0.5.cqh(m))
            .background(PrimaryRed))
        Spacer(modifier = Modifier.width(1.1.cqh(m)))
        Text(
            text = title.uppercase(),
            color = TextPrimary,
            fontSize = 2.1.cqhSp(m),
            fontWeight = FontWeight.SemiBold,
            fontFamily = Rajdhani,
            letterSpacing = 0.1.sp
        )
    }
}

@Composable
fun SettingsRow(
    label: String,
    hint: Boolean = false,
    sub: Boolean = false,
    m: Float,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 0.8.cqh(m))
            .let {
                if (sub) {
                    it.padding(start = 2.4.cqh(m))
                      .drawBehind { drawRect(color = PrimaryRed.copy(alpha = 0.6f), size = androidx.compose.ui.geometry.Size(0.4.cqh(m).toPx(), size.height)) }
                } else {
                    it
                }
            }
            .background(PanelDark)
            .padding(horizontal = 2.0.cqh(m), vertical = 1.0.cqh(m)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = TextPrimary.copy(alpha = 0.9f),
            fontSize = 2.1.cqhSp(m),
            fontFamily = Rajdhani,
            modifier = Modifier.weight(1f)
        )
        if (hint) {
            Box(
                modifier = Modifier
                    .size(2.4.cqh(m))
                    .background(PanelDark2),
                contentAlignment = Alignment.Center
            ) {
                Text("?", color = TextSecondary, fontSize = 1.6.cqhSp(m), fontFamily = Rajdhani)
            }
            Spacer(modifier = Modifier.width(1.6.cqh(m)))
        } else {
            Spacer(modifier = Modifier.width(1.6.cqh(m)))
        }
        content()
    }
}

@Composable
fun SettingsToggle(value: Boolean, m: Float, onValueChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .border(1.dp, BorderDark)
            .background(PanelDark2)
    ) {
        listOf(false, true).forEach { isTrue ->
            val isActive = value == isTrue
            Box(
                modifier = Modifier
                    .width(10.0.cqh(m))
                    .clickable { onValueChange(isTrue) }
                    .let {
                        if (isActive && isTrue) {
                            it.background(PrimaryRed)
                        } else if (isActive && !isTrue) {
                            it.background(PanelDark2)
                        } else {
                            it
                        }
                    }
                    .padding(vertical = 0.9.cqh(m)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isTrue) "On" else "Off",
                    color = if (isActive && isTrue) TextPrimary else if (isActive && !isTrue) TextPrimary else TextSecondary,
                    fontSize = 1.9.cqhSp(m),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Rajdhani
                )
            }
        }
    }
}

@Composable
fun SettingsSegmentedControl(options: List<String>, value: String, m: Float, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .border(1.dp, BorderDark)
            .background(PanelDark2)
    ) {
        options.forEach { option ->
            val isActive = value == option
            Box(
                modifier = Modifier
                    .defaultMinSize(minWidth = 9.0.cqh(m))
                    .clickable { onValueChange(option) }
                    .let {
                        if (isActive) {
                            it.background(PrimaryRed)
                        } else {
                            it
                        }
                    }
                    .padding(horizontal = 1.8.cqh(m), vertical = 0.9.cqh(m)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (isActive) TextPrimary else TextSecondary,
                    fontSize = 1.9.cqhSp(m),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Rajdhani,
                    letterSpacing = 0.04.sp
                )
            }
        }
    }
}

@Composable
fun SettingsSlider(
    label: String,
    value: Float,
    min: Float,
    max: Float,
    step: Float,
    unit: String,
    m: Float,
    onValueChange: (Float) -> Unit
) {
    SettingsRow(label = label, m = m) {
        Text(
            text = "${value.toInt()}$unit",
            color = TextPrimary,
            fontSize = 2.1.cqhSp(m),
            fontWeight = FontWeight.SemiBold,
            fontFamily = Rajdhani,
            modifier = Modifier.width(10.0.cqh(m)),
            textAlign = androidx.compose.ui.text.style.TextAlign.Right
        )
        Spacer(modifier = Modifier.width(1.6.cqh(m)))
        
        Box(
            modifier = Modifier
                .size(2.8.cqh(m))
                .background(PanelDark2)
                .clickable { onValueChange((value - step).coerceAtLeast(min)) },
            contentAlignment = Alignment.Center
        ) {
            Text("−", color = TextPrimary, fontSize = 2.0.cqhSp(m), fontFamily = Rajdhani)
        }
        
        Spacer(modifier = Modifier.width(1.0.cqh(m)))
        
        val pct = ((value - min) / (max - min)).coerceIn(0f, 1f)
        
        Box(
            modifier = Modifier
                .height(2.8.cqh(m))
                .width(40.0.cqh(m))
        ) {
            // Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.7.cqh(m))
                    .align(Alignment.Center)
                    .background(TrackDark)
            )
            // Fill
            Box(
                modifier = Modifier
                    .fillMaxWidth(pct)
                    .height(0.7.cqh(m))
                    .align(Alignment.CenterStart)
                    .background(Brush.horizontalGradient(listOf(PrimaryRedLight, PrimaryRedDark)))
            )
            // Thumb (simulated invisible slider)
            Slider(
                value = value,
                onValueChange = { onValueChange(it) },
                valueRange = min..max,
                steps = ((max - min) / step).toInt() - 1,
                modifier = Modifier.matchParentSize(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Transparent,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent,
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent
                )
            )
            // Thumb visual
            Box(
                modifier = Modifier
                    .height(2.4.cqh(m))
                    .width(0.9.cqh(m))
                    .align(Alignment.CenterStart)
                    .offset(x = (40.0.cqh(m) * pct) - (0.45.cqh(m)))
                    .background(TextPrimary)
            )
        }
        
        Spacer(modifier = Modifier.width(1.0.cqh(m)))
        
        Box(
            modifier = Modifier
                .size(2.8.cqh(m))
                .background(PanelDark2)
                .clickable { onValueChange((value + step).coerceAtMost(max)) },
            contentAlignment = Alignment.Center
        ) {
            Text("+", color = TextPrimary, fontSize = 2.0.cqhSp(m), fontFamily = Rajdhani)
        }
    }
}
