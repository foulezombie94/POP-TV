package com.poptv.launcher.prefs

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class LocalProfile(val id: String, val name: String)
data class ProfileState(val profiles: List<LocalProfile>, val activeId: String) {
    val active: LocalProfile get() = profiles.firstOrNull { it.id == activeId } ?: profiles.first()
}
object ProfileRules {
    const val MAX_PROFILES = 6
    fun select(state: ProfileState, id: String): ProfileState =
        if (state.profiles.any { it.id == id }) state.copy(activeId = id) else state
    fun add(state: ProfileState, id: String): ProfileState =
        if (state.profiles.size >= MAX_PROFILES || state.profiles.any { it.id == id }) state
        else ProfileState(state.profiles + LocalProfile(id, "Profil ${state.profiles.size + 1}"), id)
}

/** Local launcher profiles only; no access to Apple or OQEE account data. */
class ProfileStore private constructor(context: Context) {
    private val prefs = context.getSharedPreferences("local_profiles", Context.MODE_PRIVATE)
    private val mutable = MutableStateFlow(read())
    val state = mutable.asStateFlow()
    fun select(id: String) = save(ProfileRules.select(mutable.value, id))
    fun add() = save(ProfileRules.add(mutable.value, UUID.randomUUID().toString()))
    private fun read(): ProfileState = runCatching {
        val items = JSONArray(prefs.getString("items", "[]"))
        val list = (0 until items.length()).map {
            items.getJSONObject(it).let { p -> LocalProfile(p.getString("id"), p.getString("name")) }
        }.take(ProfileRules.MAX_PROFILES).ifEmpty { listOf(LocalProfile("main", "Principal")) }
        ProfileRules.select(ProfileState(list, list.first().id), prefs.getString("active", "main") ?: "main")
    }.getOrDefault(ProfileState(listOf(LocalProfile("main", "Principal")), "main"))
    private fun save(value: ProfileState) {
        val json = JSONArray()
        value.profiles.forEach { json.put(JSONObject().put("id", it.id).put("name", it.name)) }
        prefs.edit().putString("items", json.toString()).putString("active", value.activeId).apply()
        mutable.value = value
    }
    companion object {
        @Volatile private var instance: ProfileStore? = null
        fun get(context: Context): ProfileStore = instance ?: synchronized(this) {
            instance ?: ProfileStore(context.applicationContext).also { instance = it }
        }
    }
}
