package com.poptv.launcher

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AtomicFile
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/** Six real, launchable apps restored before the expensive package-manager inventory. */
class DockSnapshot(context: Context, profile: String) {
    private val directory = File(context.cacheDir, "dock-${profile.hashCode().toUInt().toString(16)}").apply { mkdirs() }
    fun read(): List<AppEntry> = runCatching {
        val rows = JSONArray(File(directory, "apps.json").readText())
        (0 until minOf(rows.length(), GridNav.COLUMNS)).map { index ->
            val row = rows.getJSONObject(index)
            val bitmap = requireNotNull(BitmapFactory.decodeFile(File(directory, "$index.png").path)).asImageBitmap()
            AppEntry(row.getString("key"), row.getString("package"), row.getString("label"), null,
                bitmap, bitmap, Intent.parseUri(row.getString("intent"), 0), row.getBoolean("removable"), row.getBoolean("pseudo"))
        }
    }.getOrDefault(emptyList())

    fun save(apps: List<AppEntry>) = runCatching {
        val rows = JSONArray()
        apps.take(GridNav.COLUMNS).forEachIndexed { index, app ->
            val atomic = AtomicFile(File(directory, "$index.png"))
            val output = atomic.startWrite()
            try {
                check(app.tile.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, output))
                atomic.finishWrite(output)
            } catch (error: Exception) { atomic.failWrite(output); throw error }
            rows.put(JSONObject().put("key", app.key).put("package", app.packageName).put("label", app.label)
                .put("intent", app.launchIntent.toUri(0)).put("removable", app.isRemovable).put("pseudo", app.isPseudo))
        }
        File(directory, "apps.json").writeText(rows.toString())
    }
}
