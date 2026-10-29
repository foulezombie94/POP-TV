package com.poptv.launcher.features.control

/** Free's privileged HOME priority outranks normal launchers on Pop. Recovery must always win. */
object HomeRoutingPolicy {
    fun redirectStockHome(packageName: String, ownsHomeRole: Boolean, mainEnabled: Boolean): Boolean =
        packageName == "net.oqee.launcher" && ownsHomeRole && mainEnabled
}
