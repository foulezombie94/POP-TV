package com.poptv.launcher.features.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poptv.launcher.AppEntry
import com.poptv.launcher.core.design.TvColors
import com.poptv.launcher.core.design.TvDims
import com.poptv.launcher.core.design.TvShapes
import com.poptv.launcher.core.design.TvType

/** Actual installed-app artwork. No catalogue credentials or made-up titles required. */
@Composable
fun LocalTopShelf(app: AppEntry?, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().height(HomeDims.LocalShelfH).padding(horizontal = TvDims.SafeH)) {
        if (app != null) {
            Row(Modifier.fillMaxSize().padding(top = HomeDims.AppsFirstTop, bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    BasicText("À portée de télécommande", style = TvType.Caption1.copy(color = TvColors.SecondaryLabel))
                    BasicText(app.label, style = TvType.Title1, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    BasicText("OK pour ouvrir · Maintenir OK pour organiser", style = TvType.Body.copy(color = TvColors.SecondaryLabel))
                }
                Image(app.tile, contentDescription = null, contentScale = ContentScale.Fit,
                    modifier = Modifier.width(HomeDims.LocalArtW).height(HomeDims.LocalArtH).clip(TvShapes.Regular))
            }
        }
    }
}
