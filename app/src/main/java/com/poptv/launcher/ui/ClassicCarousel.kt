package com.poptv.launcher.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.allowHardware
import com.poptv.launcher.R
import com.poptv.launcher.home.ShelfImages
import com.poptv.launcher.design.*
import com.poptv.launcher.tmdb.*
import kotlinx.coroutines.delay

/** Only current + next are requested. No video, parallax loop, progress ticker or 4K decode. */
@Composable
fun ClassicCarousel(items: List<TmdbItem>, active: Boolean) {
    val context = LocalContext.current
    var index by remember { mutableIntStateOf(0) }
    val item = items.getOrNull(index) ?: items.firstOrNull()
    LaunchedEffect(active, items) {
        if (!active || items.size < 2) return@LaunchedEffect
        while (true) {
            delay(CarouselPolicy.INTERVAL_MS)
            index = CarouselPolicy.next(index, items.size)
        }
    }
    fun request(title: TmdbItem) = ImageRequest.Builder(context)
        .data(TmdbClient.image(title.backdropPath)).size(1280, 720).allowHardware(true).build()
    LaunchedEffect(active, item, items) {
        if (active && items.size > 1) {
            // A single background prefetch, cancelled when the surface is paused.
            SingletonImageLoader.get(context).execute(request(items[CarouselPolicy.next(index, items.size)]))
            items[CarouselPolicy.next(index, items.size)].homeLogo?.let {
                SingletonImageLoader.get(context).execute(ShelfImages.logo(context, TmdbClient.image(it, "w500")!!))
            }
        }
    }
    if (item == null) {
        Image(painterResource(R.drawable.foundation_backdrop), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        Image(painterResource(R.drawable.foundation_logo), "Foundation",
            Modifier.padding(start = TvDims.SafeH, top = TvDims.SafeV).width(ClassicDims.LogoW).height(ClassicDims.LogoH),
            contentScale = ContentScale.Fit, colorFilter = ColorFilter.tint(TvColors.Label))
        return
    }
    key(item.id, item.mediaType) {
        val image = remember(item.backdropPath) { request(item) }
        Box(Modifier.fillMaxSize()) {
            AsyncImage(image, item.title, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Box(Modifier.fillMaxSize().background(CarouselDims.Scrim))
            Box(Modifier.align(Alignment.BottomStart).padding(start = TvDims.SafeH, bottom = CarouselDims.TitleBottom)
                .size(CarouselDims.LogoW, CarouselDims.LogoH), contentAlignment = Alignment.BottomStart) {
                CarouselTitle(item)
            }
        }
    }
    Box(Modifier.fillMaxWidth().padding(top = CarouselDims.DotsTop), contentAlignment = Alignment.TopCenter) {
        // Apple's page control: equal centres, solid current dot, quiet material beneath.
        // One draw operation, no animated progress bar waking the GPU throughout seven seconds.
        Canvas(Modifier.size(CarouselDims.DotsInsetH * 2 + CarouselDims.DotStep * items.size, CarouselDims.DotsHeight)
            .semantics { contentDescription = "Affiche ${index + 1} sur ${items.size}" }) {
            drawRoundRect(TvColors.MaterialThin, cornerRadius = CornerRadius(size.height / 2))
            repeat(items.size) { dot ->
                val center = Offset(CarouselDims.DotsInsetH.toPx() + CarouselDims.DotStep.toPx() * (dot + 0.5f), size.height / 2)
                drawCircle(Color.Black.copy(alpha = 0.3f), CarouselDims.DotRadius.toPx() + 1.dp.toPx(), center)
                drawCircle(TvColors.Label.copy(alpha = if (dot == index) 1f else 0.32f),
                    if (dot == index) CarouselDims.DotRadius.toPx() else CarouselDims.QuietDotRadius.toPx(), center)
            }
        }
    }
}

@Composable
private fun CarouselTitle(item: TmdbItem) {
    val context = LocalContext.current
    if (item.homeLogo != null) {
        val request = remember(item.homeLogo) { ShelfImages.logo(context, TmdbClient.image(item.homeLogo, "w500")!!) }
        val painter = rememberAsyncImagePainter(request, contentScale = ContentScale.Fit)
        val state by painter.state.collectAsState()
        if (state !is AsyncImagePainter.State.Error) {
            Image(painter, item.title, Modifier.fillMaxSize(), alignment = Alignment.BottomStart, contentScale = ContentScale.Fit)
            return
        }
    }
    BasicText(item.title, style = TvType.Title2, maxLines = 2, overflow = TextOverflow.Ellipsis)
}

/** Quiet artwork-led Top Shelf, with no full-screen blending or runtime blur on the Pop GPU. */
private object CarouselDims {
    val LogoW = 250.dp
    val LogoH = 75.dp
    val TitleBottom = 175.dp
    val DotsTop = 375.dp
    val DotRadius = 2.5.dp
    val QuietDotRadius = 2.dp
    val DotStep = 10.dp
    val DotsInsetH = 6.dp
    val DotsHeight = 14.dp
    val Scrim = Brush.verticalGradient(0f to Color.Black.copy(alpha = 0.12f),
        0.3f to Color.Transparent, 0.5f to Color.Transparent, 0.78f to Color.Black.copy(alpha = 0.4f),
        1f to Color.Black.copy(alpha = 0.76f))
}
