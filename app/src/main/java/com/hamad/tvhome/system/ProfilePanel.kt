package com.hamad.tvhome.system

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hamad.tvhome.R
import com.hamad.tvhome.design.*
import com.hamad.tvhome.prefs.ProfileState
import com.hamad.tvhome.prefs.ProfileRules
import com.hamad.tvhome.ui.ProfileAvatar
import com.hamad.tvhome.ui.ProfileDims
import com.hamad.tvhome.ui.rememberClockText

@Composable
fun PanelTabs(profiles: Boolean, headerFocused: Boolean, name: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        BasicText(rememberClockText(), style = TvType.Callout, modifier = Modifier.weight(1f))
        Box(Modifier.size(36.dp).background(if (profiles) TvColors.IdleFill else androidx.compose.ui.graphics.Color.Transparent, TvShapes.Capsule)
            .border(if (profiles && headerFocused) 1.dp else 0.dp, TvColors.Label, TvShapes.Capsule),
            contentAlignment = Alignment.Center) { ProfileAvatar(name, 26.dp) }
        Box(Modifier.size(36.dp).background(if (!profiles) TvColors.IdleFill else androidx.compose.ui.graphics.Color.Transparent, TvShapes.Capsule)
            .border(if (!profiles && headerFocused) 1.dp else 0.dp, TvColors.Label, TvShapes.Capsule),
            contentAlignment = Alignment.Center) {
            Image(painterResource(R.drawable.sys_gear), "Commandes", Modifier.size(21.dp), colorFilter = ColorFilter.tint(TvColors.Label))
        }
    }
}

@Composable
fun ProfilePanel(state: ProfileState, focused: Int, headerFocused: Boolean) {
    val count = state.profiles.size + if (state.profiles.size < ProfileRules.MAX_PROFILES) 1 else 0
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (r in 0 until (count + 1) / 2) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (c in 0..1) {
                    val index = r * 2 + c
                    if (index >= count) { Spacer(Modifier.weight(1f)); continue }
                    val p = state.profiles.getOrNull(index)
                    val active = p?.id == state.activeId
                    val focus = !headerFocused && index == focused
                    Column(Modifier.weight(1f).height(ProfileDims.PickerCellH)
                        .background(if (focus) TvColors.IdleFill else androidx.compose.ui.graphics.Color.Transparent, TvShapes.Regular)
                        .padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Box {
                            if (p != null) ProfileAvatar(p.name, ProfileDims.PickerAvatar, focus)
                            else Box(Modifier.size(ProfileDims.PickerAvatar).background(TvColors.MaterialThick, CircleShape)
                                .border(if (focus) 1.dp else 0.dp, TvColors.Label, CircleShape), contentAlignment = Alignment.Center) {
                                BasicText("+", style = TvType.Title1)
                            }
                            if (active) Box(Modifier.align(Alignment.TopEnd).size(17.dp).background(TvColors.Green, CircleShape),
                                contentAlignment = Alignment.Center) { BasicText("✓", style = TvType.Caption2) }
                        }
                        BasicText(p?.name ?: "Ajouter un profil", style = TvType.Caption1.copy(textAlign = TextAlign.Center), maxLines = 2)
                    }
                }
            }
        }
        BasicText("Profils de cet accueil", style = TvType.Caption2.copy(color = TvColors.SecondaryLabel))
    }
}
