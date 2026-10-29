package com.poptv.launcher.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.poptv.launcher.R
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.poptv.launcher.GridNav
import com.poptv.launcher.HomeViewModel
import com.poptv.launcher.design.TvColors
import com.poptv.launcher.design.TvDims
import com.poptv.launcher.design.TvShapes
import com.poptv.launcher.home.HomeGridLayout
import com.poptv.launcher.home.Tab
import kotlinx.coroutines.flow.collectLatest

/** The tvOS launcher, distinct from the Apple TV catalogue app: dock + scrollable icon grid. */
@Composable
fun ClassicHomeScreen(vm: HomeViewModel) {
    val focus = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    val keys = remember { KeyRouter(vm, scope) }
    val page = vm.pages.getValue(Tab.Apps)
    val scroll = remember { Animatable(0f) }
    val idle = rememberIdle(vm)
    LaunchedEffect(Unit) { focus.requestFocus() }
    LaunchedEffect(page) {
        snapshotFlow { HomeGridLayout.scrollFor(page.gridFocus) }
            .collectLatest { scroll.animateTo(it, Motion.scroll()) }
    }
    val shown by remember { derivedStateOf { HomeGridLayout.visibleIndices(scroll.value, vm.apps.size) } }
    Box(Modifier.fillMaxSize().background(TvColors.AppBackground)
        .focusRequester(focus).focusable().onPreviewKeyEvent { keys.handle(it.nativeKeyEvent) }) {
        // Keep the carousel's state while scrolling: removing it resets its page on every Down/Up.
        // Rotation and prefetch stop offscreen; alpha zero skips painting the hidden artwork.
        val carouselActive = com.poptv.launcher.tmdb.CarouselPolicy.active(
            vm.resumed, page.gridFocus < GridNav.COLUMNS, vm.profileFocused, vm.editing, vm.sheet != null)
        Box(Modifier.fillMaxSize().graphicsLayer {
            alpha = (1f - scroll.value / (HomeGridLayout.EXPANDED_TOP - HomeGridLayout.COLLAPSED_TOP)).coerceIn(0f, 1f)
        }) {
            ClassicCarousel(vm.carousel, carouselActive)
        }
        val context = LocalContext.current
        ProfilePill(vm.profileFocused, Modifier.align(Alignment.TopEnd).padding(top = TvDims.SafeV, end = TvDims.SafeH)) {
            context.startActivity(android.content.Intent(context, com.poptv.launcher.system.ControlCenterActivity::class.java)
                .putExtra("profiles", true))
        }
        CompositionLocalProvider(LocalIdle provides { idle.value }) {
            Box(Modifier.fillMaxSize().graphicsLayer { translationY = -scroll.value.dp.toPx() }) {
                // One translucent dock groups exactly the first six apps, as on Apple TV Home.
                Box(Modifier.offset(x = ClassicDims.DockX, y = ClassicDims.DockTop)
                    .size(ClassicDims.DockW, ClassicDims.DockH)
                    .background(TvColors.MaterialUltraThin, TvShapes.Large)
                    .border(ClassicDims.DockStroke, TvColors.Separator.copy(alpha = 0.10f), TvShapes.Large))
                for (i in shown) {
                    val app = vm.apps[i]
                    key(app.key) {
                        val selected = page.row >= 0 && i == page.gridFocus && !vm.profileFocused
                        AppTile(app, i, Dims.tileX(i % GridNav.COLUMNS),
                            ClassicDims.FirstRowTop + ClassicDims.RowPitch * (i / GridNav.COLUMNS),
                            selected, false, vm.editing, selected && vm.pressedKey == app.key,
                            if (selected) vm.nudge else null)
                    }
                }
            }
        }
        EditHint(vm)
        ActionSheetOverlay(vm.sheet)
        if (vm.apps.isEmpty()) Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            BasicText("Chargement des applications…", style = Type.Quiet)
        }
    }
}

/** Geometry follows Apple's six-column default at 1920×1080 / 320 dpi. */
object ClassicDims {
    val FirstRowTop = HomeGridLayout.EXPANDED_TOP.dp
    val RowPitch = HomeGridLayout.ROW_PITCH.dp
    val ShelfH = 250.dp
    val LogoW = 280.dp
    val LogoH = 60.dp
    val PreviewW = 280.dp
    val PreviewH = 168.dp
    val DockX = 22.dp
    val DockTop = FirstRowTop - 18.dp
    val DockW = 916.dp
    val DockH = 114.dp
    val DockStroke = 0.5.dp
}
