package com.poptv.launcher

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AtomicFile
import java.io.File

/** Private, bounded artwork cache. APK path changes on updates, invalidating old pictures. */
class AppArtCache(context: Context) {
    private val directory = File(context.cacheDir, "app-art-v1").apply { mkdirs() }
    fun tile(key: String, render: () -> Bitmap): Bitmap {
        val file = File(directory, "${key.hashCode().toUInt().toString(16)}.png")
        runCatching { BitmapFactory.decodeFile(file.path) }.getOrNull()?.let { return it }
        return render().also { bitmap ->
            runCatching {
                val atomic = AtomicFile(file)
                val output = atomic.startWrite()
                try {
                    check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, output))
                    atomic.finishWrite(output)
                } catch (error: Exception) { atomic.failWrite(output); throw error }
            }
        }
    }
    fun trim() {
        directory.listFiles()?.sortedByDescending { it.lastModified() }?.drop(80)?.forEach { it.delete() }
    }
}
