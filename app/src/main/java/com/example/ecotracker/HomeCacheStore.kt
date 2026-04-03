package com.example.ecotracker

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object HomeCacheStore {
    private const val PREFS = "home_cache_prefs"
    private const val KEY_UID = "uid"
    private const val KEY_PAYLOAD = "payload"
    private const val KEY_TS = "cached_at"
    private const val TTL_MS = 15 * 60 * 1000L

    fun write(context: Context, uid: String, data: HomeData) {
        val payload = JSONObject()
            .put("userName", data.userName)
            .put("tips", JSONArray(data.tips))
            .put("footprint", data.footprint)
            .put("challengesParticipated", data.challengesParticipated)
            .put("treesSaved", data.treesSaved)
            .put("emissionReduced", data.emissionReduced)
            .put("quote", data.quote)
            .put("onboardingCompleted", data.onboardingCompleted)
            .put("photoBase64", data.photoBase64)
            .toString()

        prefs(context)
            .edit()
            .putString(KEY_UID, uid)
            .putString(KEY_PAYLOAD, payload)
            .putLong(KEY_TS, System.currentTimeMillis())
            .apply()
    }

    fun read(context: Context, uid: String): HomeData? {
        val prefs = prefs(context)
        val cachedUid = prefs.getString(KEY_UID, null) ?: return null
        if (cachedUid != uid) return null

        val payload = prefs.getString(KEY_PAYLOAD, null) ?: return null
        return parse(payload, prefs.getLong(KEY_TS, 0L))
    }

    fun invalidate(context: Context) {
        prefs(context).edit().clear().apply()
    }

    fun isExpired(cachedAtMs: Long): Boolean {
        if (cachedAtMs <= 0L) return true
        return System.currentTimeMillis() - cachedAtMs > TTL_MS
    }

    private fun parse(payload: String, cachedAtMs: Long): HomeData? {
        return try {
            val json = JSONObject(payload)
            val tipsArray = json.optJSONArray("tips") ?: JSONArray()
            val tips = buildList {
                for (i in 0 until tipsArray.length()) {
                    add(tipsArray.optString(i))
                }
            }
            HomeData(
                userName = json.optString("userName"),
                tips = tips,
                footprint = json.optString("footprint"),
                challengesParticipated = json.optString("challengesParticipated"),
                treesSaved = json.optString("treesSaved"),
                emissionReduced = json.optString("emissionReduced"),
                quote = json.optString("quote"),
                onboardingCompleted = json.optBoolean("onboardingCompleted", false),
                photoBase64 = json.optString("photoBase64").ifBlank { null },
                cachedAtMs = cachedAtMs
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
