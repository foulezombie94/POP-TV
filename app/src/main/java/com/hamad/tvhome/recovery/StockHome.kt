package com.hamad.tvhome.recovery

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings

/** Recovery changes only our own components/service; Free stays installed and enabled. */
object StockHome {
    val component = ComponentName("net.oqee.launcher", "net.oqee.tvlauncher.ui.home.HomeActivity")
    const val STOP_CONTROLS = "com.hamad.tvhome.STOP_CONTROLS"
    fun available(context: Context): Boolean = runCatching {
        context.packageManager.getActivityInfo(component, 0).enabled
    }.getOrDefault(false)
    fun open(context: Context) = context.startActivity(
        Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setComponent(component)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED),
    )
    fun restore(context: Context) {
        context.sendBroadcast(Intent(STOP_CONTROLS).setPackage(context.packageName))
        runCatching {
            val key = Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            val own = ComponentName(context.packageName, "com.hamad.tvhome.system.SystemUiService")
            val remaining = Settings.Secure.getString(context.contentResolver, key).orEmpty()
                .split(':').filter { it.isNotBlank() && ComponentName.unflattenFromString(it) != own }
            Settings.Secure.putString(context.contentResolver, key, remaining.joinToString(":"))
        }
        context.packageManager.setComponentEnabledSetting(
            ComponentName(context.packageName, "com.hamad.tvhome.MainActivity"),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP,
        )
        // Withdraw ours before launching Free, preventing the optional service from redirecting it.
        open(context)
    }
    fun enable(context: Context) = context.packageManager.setComponentEnabledSetting(
        ComponentName(context.packageName, "com.hamad.tvhome.MainActivity"),
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP,
    )
}
