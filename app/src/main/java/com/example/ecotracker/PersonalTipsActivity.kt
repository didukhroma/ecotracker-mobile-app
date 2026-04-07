package com.example.ecotracker

import android.content.Context
import android.os.Bundle
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

class PersonalTipsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_tips)

        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }

        val tipsContainer: LinearLayout = findViewById(R.id.tipsContainer)
        val stats = PersonalTipsStore.getStats(this)
        findViewById<TextView>(R.id.tipsDailySummary).text = getString(
            R.string.personal_tips_daily_summary,
            stats.todaySelectedIds.size,
            PersonalTipsRepository.getTips(this).size,
            stats.currentStreak
        )
        val tips = PersonalTipsRepository.getTips(this)
        val inflater = LayoutInflater.from(this)

        tips.forEach { tip ->
            val row = inflater.inflate(R.layout.item_personal_tip, tipsContainer, false)
            bindTipImage(row.findViewById(R.id.tipImage), tip.imageAssetPath)
            row.findViewById<TextView>(R.id.tipTitle).text = tip.title
            row.findViewById<TextView>(R.id.tipDescription).text = tip.description
            row.findViewById<CheckBox>(R.id.tipCheckbox).apply {
                isChecked = tip.selected
                setOnCheckedChangeListener { _, isChecked ->
                    PersonalTipsStore.setSelected(this@PersonalTipsActivity, tip.id, isChecked)
                }
            }
            tipsContainer.addView(row)
        }
    }

    private fun bindTipImage(imageView: ImageView, assetPath: String) {
        val bitmap = try {
            assets.open(assetPath).use { input -> BitmapFactory.decodeStream(input) }
        } catch (_: Exception) {
            null
        }
        imageView.setImageBitmap(bitmap)
    }
}

data class PersonalTipItem(
    val id: String,
    val title: String,
    val description: String,
    val imageAssetPath: String,
    val selected: Boolean
)

object PersonalTipsRepository {
    // Integration point for server-provided personalized recommendations.
    fun getTips(context: Context): List<PersonalTipItem> {
        val selectedIds = PersonalTipsStore.getSelectedIds(context)
        return listOf(
            PersonalTipItem(
                id = "transport_tip",
                title = "Transport",
                description = "Go to joint trips (Carpooling) or use public transport.",
                imageAssetPath = "personal_tips/carpool.png",
                selected = selectedIds.contains("transport_tip")
            ),
            PersonalTipItem(
                id = "energy_tip",
                title = "Energy consumption",
                description = "Install energy efficient lamps (such as LED lamps) instead of ordinary ones.",
                imageAssetPath = "personal_tips/energy.jpg",
                selected = selectedIds.contains("energy_tip")
            ),
            PersonalTipItem(
                id = "food_tip",
                title = "Food",
                description = "Reduce meat consumption, especially red, and add more plant foods to your diet.",
                imageAssetPath = "personal_tips/food.jpg",
                selected = selectedIds.contains("food_tip")
            ),
            PersonalTipItem(
                id = "shopping_tip",
                title = "Shopping",
                description = "Avoid single-use plastic products.",
                imageAssetPath = "personal_tips/shopping.jpg",
                selected = selectedIds.contains("shopping_tip")
            ),
            PersonalTipItem(
                id = "water_tip",
                title = "Water",
                description = "Install aerators on faucets and reduce water usage.",
                imageAssetPath = "personal_tips/water.jpg",
                selected = selectedIds.contains("water_tip")
            ),
            PersonalTipItem(
                id = "trees_tip",
                title = "Trees",
                description = "Choose a native species that fits your local climate and support biodiversity in your area.",
                imageAssetPath = "personal_tips/trees.jpg",
                selected = selectedIds.contains("trees_tip")
            )
        )
    }
}

data class PersonalTipsStats(
    val todayKey: String,
    val todaySelectedIds: Set<String>,
    val dailyCompletions: Map<String, Set<String>>,
    val totalCompletionEvents: Int,
    val currentStreak: Int,
    val bestStreak: Int,
    val activeDaysCount: Int
)

object PersonalTipsStore {
    private const val PREFS_NAME = "personal_tips_state"
    private const val KEY_DAILY_COMPLETIONS = "daily_completions"
    private const val KEY_SELECTED_IDS_LEGACY = "selected_tip_ids"

    fun currentDateKey(): String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    fun getTodaySelectedIds(context: Context): Set<String> = getStats(context).todaySelectedIds

    fun getSelectedIds(context: Context): Set<String> {
        return getTodaySelectedIds(context)
    }

    fun isSelected(context: Context, tipId: String): Boolean = getSelectedIds(context).contains(tipId)

    fun selectedCount(context: Context): Int = getSelectedIds(context).size

    fun totalCompletionEvents(context: Context): Int = getStats(context).totalCompletionEvents

    fun activeDaysCount(context: Context): Int = getStats(context).activeDaysCount

    fun currentStreak(context: Context): Int = getStats(context).currentStreak

    fun bestStreak(context: Context): Int = getStats(context).bestStreak

    fun completionCountForTip(context: Context, tipId: String): Int {
        return getStats(context).dailyCompletions.values.count { it.contains(tipId) }
    }

    fun hasEverCompleted(context: Context, tipId: String): Boolean = completionCountForTip(context, tipId) > 0

    fun uniqueCompletedTipsCount(context: Context): Int {
        val allIds = getStats(context).dailyCompletions.values.flatten().toSet()
        return allIds.size
    }

    fun getStats(context: Context): PersonalTipsStats {
        val daily = migrateAndReadDailyCompletions(context)
        val todayKey = currentDateKey()
        val sortedDates = daily.keys.sorted()
        val currentStreak = computeCurrentStreak(sortedDates, todayKey)
        val bestStreak = computeBestStreak(sortedDates)
        val totalCompletionEvents = daily.values.sumOf { it.size }
        return PersonalTipsStats(
            todayKey = todayKey,
            todaySelectedIds = daily[todayKey].orEmpty(),
            dailyCompletions = daily,
            totalCompletionEvents = totalCompletionEvents,
            currentStreak = currentStreak,
            bestStreak = bestStreak,
            activeDaysCount = daily.size
        )
    }

    fun setSelected(context: Context, tipId: String, selected: Boolean) {
        val daily = migrateAndReadDailyCompletions(context).toMutableMap()
        val todayKey = currentDateKey()
        val updated = daily[todayKey].orEmpty().toMutableSet().apply {
            if (selected) add(tipId) else remove(tipId)
        }
        if (updated.isEmpty()) {
            daily.remove(todayKey)
        } else {
            daily[todayKey] = updated
        }
        persist(context, daily)
        FirebaseSync.syncPersonalTips(context)
    }

    fun setSelectedIds(context: Context, ids: Set<String>) {
        persist(context, mapOf(currentDateKey() to ids))
    }

    fun setDailyCompletions(context: Context, dailyCompletions: Map<String, Set<String>>) {
        persist(context, dailyCompletions)
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_DAILY_COMPLETIONS)
            .remove(KEY_SELECTED_IDS_LEGACY)
            .apply()
    }

    private fun migrateAndReadDailyCompletions(context: Context): Map<String, Set<String>> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_DAILY_COMPLETIONS, null)
        if (!raw.isNullOrBlank()) {
            return decodeDailyCompletions(raw)
        }

        val legacy = prefs.getStringSet(KEY_SELECTED_IDS_LEGACY, emptySet())?.toSet().orEmpty()
        if (legacy.isNotEmpty()) {
            val migrated = mapOf(currentDateKey() to legacy)
            persist(context, migrated)
            prefs.edit().remove(KEY_SELECTED_IDS_LEGACY).apply()
            return migrated
        }
        return emptyMap()
    }

    private fun decodeDailyCompletions(raw: String): Map<String, Set<String>> {
        return try {
            val json = JSONObject(raw)
            buildMap {
                json.keys().forEach { dateKey ->
                    val ids = json.optJSONArray(dateKey)?.toStringSet().orEmpty()
                    if (ids.isNotEmpty()) put(dateKey, ids)
                }
            }
        } catch (_: Exception) {
            emptyMap()
        }
    }

    private fun persist(context: Context, dailyCompletions: Map<String, Set<String>>) {
        val json = JSONObject()
        dailyCompletions.toSortedMap().forEach { (dateKey, ids) ->
            json.put(dateKey, JSONArray(ids.toList().sorted()))
        }
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_DAILY_COMPLETIONS, json.toString())
            .apply()
    }

    private fun JSONArray.toStringSet(): Set<String> {
        return buildSet {
            for (index in 0 until length()) {
                add(optString(index))
            }
        }.filter { it.isNotBlank() }.toSet()
    }

    private fun computeCurrentStreak(sortedDates: List<String>, todayKey: String): Int {
        if (sortedDates.isEmpty()) return 0
        var cursor = todayKey
        var streak = 0
        while (sortedDates.contains(cursor)) {
            streak += 1
            cursor = previousDateKey(cursor) ?: break
        }
        return streak
    }

    private fun computeBestStreak(sortedDates: List<String>): Int {
        if (sortedDates.isEmpty()) return 0
        var best = 0
        var current = 0
        var previous: String? = null
        sortedDates.forEach { dateKey ->
            current = if (previous != null && previousDateKey(dateKey) == previous) {
                current + 1
            } else {
                1
            }
            best = max(best, current)
            previous = dateKey
        }
        return best
    }

    private fun previousDateKey(dateKey: String): String? {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date = format.parse(dateKey) ?: return null
            val previous = Date(date.time - 24L * 60L * 60L * 1000L)
            format.format(previous)
        } catch (_: Exception) {
            null
        }
    }
}
