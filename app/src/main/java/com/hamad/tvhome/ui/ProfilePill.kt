package com.hamad.tvhome.ui

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hamad.tvhome.design.*
import com.hamad.tvhome.design.LiquidGlass.liquidGlass
import com.hamad.tvhome.prefs.ProfileStore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.delay

@Composable
fun rememberClockText(): String {
    val context = LocalContext.current
    val pattern = if (DateFormat.is24HourFormat(context)) "HH:mm" else "h:mm"
    val format = remember(pattern) { SimpleDateFormat(pattern, Locale.FRANCE) }
    var text by remember { mutableStateOf(format.format(Date())) }
    LaunchedEffect(pattern) {
        while (true) {
            text = format.format(Date())
            delay(60_000 - System.currentTimeMillis() % 60_000 + 50)
        }
    }
    return text
}

@Composable
fun ProfilePill(focused: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val profile by ProfileStore.get(LocalContext.current).state.collectAsState()
    Row(modifier.graphicsLayer { scaleX = if (focused) 1.08f else 1f; scaleY = scaleX }
        .height(ProfileDims.PillH).liquidGlass(TvShapes.Capsule, 0.45f)
        .background(if (focused) TvColors.FocusedFill else TvColors.MaterialThin, TvShapes.Capsule)
        .clickable(onClick = onClick).padding(start = 13.dp, end = 5.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        BasicText(rememberClockText(), style = TvType.Callout.copy(
            color = if (focused) TvColors.FocusedLabel else TvColors.Label))
        ProfileAvatar(profile.active.name, ProfileDims.PillAvatar)
    }
}

@Composable
fun ProfileAvatar(name: String, size: androidx.compose.ui.unit.Dp, selected: Boolean = false) {
    Box(Modifier.size(size).background(TvColors.MaterialRegular, CircleShape)
        .border(1.dp, if (selected) TvColors.Green else TvColors.Label.copy(alpha = 0.7f), CircleShape),
        contentAlignment = Alignment.Center) {
        BasicText(name.substringAfterLast(' ').take(1).uppercase(), style = if (size > 40.dp) TvType.Title2 else TvType.Caption2)
    }
}

/** Capsule and avatar metrics follow the compact tvOS status/profile affordance. */
object ProfileDims {
    val PillH = 32.dp
    val PillAvatar = 24.dp
    val PickerAvatar = 62.dp
    val PickerCellH = 116.dp
}
