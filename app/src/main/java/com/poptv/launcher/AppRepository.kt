package com.poptv.launcher

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.ContextCompat
import com.poptv.launcher.features.search.SearchActivity
import com.poptv.launcher.features.settings.SettingsActivity

class AppRepository(private val context: Context) {
    private val pm: PackageManager = context.packageManager
    private val art = AppArtCache(context)
    fun peek(): List<AppEntry> = snapshot

    fun load(): List<AppEntry> {
        val seen = HashSet<String>()
        val entries = ArrayList<AppEntry>()
        val candidates = query(Intent.CATEGORY_LEANBACK_LAUNCHER).map { it to true } +
            query(Intent.CATEGORY_LAUNCHER).map { it to false }
        for ((info, isLeanback) in candidates) {
            val pkg = info.activityInfo.packageName
            if (pkg == context.packageName || !seen.add(pkg)) continue
            val signature = "$pkg/${info.activityInfo.name}/${info.activityInfo.applicationInfo.sourceDir}"
            runCatching { entriesCache[signature] ?: build(info, isLeanback, signature).also { entriesCache.put(signature, it) } }
                .onSuccess { entries += it }
                .onFailure { Log.w(TAG, "Skipping $pkg", it) }
        }
        (entriesCache[SETTINGS_KEY] ?: settingsEntry()?.also { entriesCache.put(SETTINGS_KEY, it) })?.let { entries += it }
        (entriesCache[SEARCH_KEY] ?: searchEntry()?.also { entriesCache.put(SEARCH_KEY, it) })?.let { entries += it }
        snapshot = entries
        art.trim()
        return entries
    }

    private fun query(category: String): List<ResolveInfo> =
        pm.queryIntentActivities(Intent(Intent.ACTION_MAIN).addCategory(category), 0)

    private fun build(info: ResolveInfo, isLeanback: Boolean, signature: String): AppEntry {
        val activity = info.activityInfo
        val app = activity.applicationInfo
        val tile = art.tile(signature) {
        val banner = if (isLeanback) activity.loadBanner(pm) ?: app.loadBanner(pm) else null
        if (banner != null) {
            Bitmaps.render(banner, BANNER_W, BANNER_H)
        } else {
            val iconBmp = Bitmaps.render(info.loadIcon(pm), ICON_PX, ICON_PX)
            Bitmaps.iconPlate(info.loadIcon(pm), Bitmaps.averageColor(iconBmp))
        }
        }
        val category = Intent.CATEGORY_LEANBACK_LAUNCHER.takeIf { isLeanback } ?: Intent.CATEGORY_LAUNCHER
        val launch = Intent(Intent.ACTION_MAIN)
            .addCategory(category)
            .setClassName(activity.packageName, activity.name)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        return AppEntry(
            key = activity.packageName,
            packageName = activity.packageName,
            label = info.loadLabel(pm).toString(),
            category = ApplicationInfo.getCategoryTitle(context, app.category)?.toString(),
            tile = tile.asImageBitmap(),
            backdrop = Bitmaps.blurred(tile).asImageBitmap(),
            launchIntent = launch,
            isRemovable = (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0,
        )
    }

    private fun searchEntry(): AppEntry? {
        val glass = ContextCompat.getDrawable(context, R.drawable.ic_search) ?: return null
        val plate = Bitmaps.searchPlate(glass)
        return AppEntry(
            key = SEARCH_KEY,
            packageName = SEARCH_KEY,
            label = "Rechercher",
            category = null,
            tile = plate.asImageBitmap(),
            backdrop = Bitmaps.blurred(plate).asImageBitmap(),
            launchIntent = Intent(context, SearchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            isRemovable = false,
            isPseudo = true,
        )
    }

    private fun settingsEntry(): AppEntry? {
        val intent = Intent(context, SettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val gear = ContextCompat.getDrawable(context, R.drawable.ic_gear) ?: return null
        val plate = Bitmaps.settingsPlate(gear)
        return AppEntry(
            key = SETTINGS_KEY,
            packageName = SETTINGS_KEY,
            label = "Réglages",
            category = null,
            tile = plate.asImageBitmap(),
            backdrop = Bitmaps.blurred(plate).asImageBitmap(),
            launchIntent = intent,
            isRemovable = false,
            isPseudo = true,
        )
    }

    companion object {
        @Volatile private var snapshot: List<AppEntry> = emptyList()
        private val entriesCache = android.util.LruCache<String, AppEntry>(60)
        const val SETTINGS_KEY = "poptv:settings"
        const val SEARCH_KEY = "poptv:search"
        private const val TAG = "PopTv"
        private const val BANNER_W = 320
        private const val BANNER_H = 180
        private const val ICON_PX = 192
    }
}
