package com.poptv.launcher.features.control

/** Free's privileged HOME priority outranks normal launchers on Pop. Recovery must always win. */
object HomeRoutingPolicy {
    private const val STOCK_HOME = "net.oqee.launcher"

    /** Cheap pre-check so the per-window-event path skips the role/component binder calls. */
    fun isStockHome(packageName: String): Boolean = packageName == STOCK_HOME

    fun redirectStockHome(packageName: String, ownsHomeRole: Boolean, mainEnabled: Boolean): Boolean =
        isStockHome(packageName) && ownsHomeRole && mainEnabled
}
