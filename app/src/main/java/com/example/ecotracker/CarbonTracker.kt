package com.example.ecotracker

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max

data class CarbonCategoryScore(
    val id: String,
    val title: String,
    val colorRes: Int,
    val kgPerYear: Double
)

data class CarbonRecommendation(
    val title: String,
    val description: String
)

data class CarbonCheckIn(
    val monthKey: String,
    val carKmPerMonth: Int,
    val flightsPerYear: Int,
    val homeEnergyDeltaPercent: Int,
    val shoppingDeltaPercent: Int,
    val savedAtMs: Long
)

data class CarbonHistoryEntry(
    val monthKey: String,
    val totalKgPerYear: Double,
    val createdAtMs: Long
)

data class CarbonTrackerSnapshot(
    val totalKgPerYear: Double,
    val countryAverageKgPerYear: Double,
    val globalAverageKgPerYear: Double,
    val comparisonVsCountry: String,
    val comparisonVsGlobal: String,
    val categories: List<CarbonCategoryScore>,
    val natureOffsetKgPerYear: Double,
    val recommendations: List<CarbonRecommendation>,
    val confidenceLabel: String,
    val latestCheckIn: CarbonCheckIn? = null
)

object CarbonTrackerCalculator {

    fun calculate(context: Context, answers: OnboardingAnswers?): CarbonTrackerSnapshot {
        val latestCheckIn = CarbonTrackerStore.getLatestCheckIn(context)
        val selectedTips = PersonalTipsStore.getTodaySelectedIds(context)
        val tipsStats = PersonalTipsStore.getStats(context)
        val completedLessons = LearningProgressStore.getCompletedIds(context)

        val transport = calculateTransport(answers, latestCheckIn, selectedTips, completedLessons, tipsStats.currentStreak)
        val food = calculateFood(answers, selectedTips, completedLessons, tipsStats.currentStreak)
        val home = calculateHome(answers, latestCheckIn, selectedTips, completedLessons, tipsStats.currentStreak)
        val purchases = calculatePurchases(latestCheckIn, selectedTips, completedLessons, tipsStats.currentStreak)
        val services = calculateServices(selectedTips, completedLessons, tipsStats.currentStreak)
        val natureOffset = calculateNatureOffset(selectedTips, completedLessons, tipsStats.currentStreak)

        val categories = listOf(
            CarbonCategoryScore("transport", "Transport", R.color.chart_blue, transport),
            CarbonCategoryScore("food", "Food", R.color.chart_green, food),
            CarbonCategoryScore("home", "Home", R.color.chart_yellow, home),
            CarbonCategoryScore("purchases", "Purchases", R.color.chart_red, purchases),
            CarbonCategoryScore("services", "Services", R.color.chart_light_blue, services)
        )

        val grossTotal = categories.sumOf { it.kgPerYear }
        val total = max(0.0, grossTotal - natureOffset)
        val countryAverage = resolveCountryAverageKg(answers?.country)
        val globalAverage = 5100.0

        return CarbonTrackerSnapshot(
            totalKgPerYear = total,
            countryAverageKgPerYear = countryAverage,
            globalAverageKgPerYear = globalAverage,
            comparisonVsCountry = formatComparison(total, countryAverage),
            comparisonVsGlobal = formatComparison(total, globalAverage),
            categories = categories,
            natureOffsetKgPerYear = natureOffset,
            recommendations = buildRecommendations(answers, categories, selectedTips, completedLessons),
            confidenceLabel = if (answers == null) "Low confidence" else "Estimated from onboarding, check-ins, and activity",
            latestCheckIn = latestCheckIn
        )
    }

    private fun calculateTransport(
        answers: OnboardingAnswers?,
        checkIn: CarbonCheckIn?,
        selectedTips: Set<String>,
        completedLessons: Set<String>,
        currentStreak: Int
    ): Double {
        val base = if (answers?.drivesCar.equals("Yes", ignoreCase = true)) {
            val frequency = when (answers?.drivingFrequency) {
                "Every day" -> 6200.0
                "5-6 times a week" -> 5100.0
                "3-4 times a week" -> 3900.0
                "1-2 times a week" -> 2400.0
                "A few times a month" -> 1100.0
                else -> 3200.0
            }
            val carMultiplier = when (answers?.carType) {
                "Diesel car" -> 1.08
                "Hybrid car" -> 0.74
                "Plug-in hybrid" -> 0.58
                "Electric car" -> 0.28
                "LPG / Gas car" -> 0.86
                else -> 1.0
            }
            frequency * carMultiplier
        } else {
            900.0
        }
        val kmAdjustment = when {
            checkIn == null -> 1.0
            checkIn.carKmPerMonth <= 200 -> 0.58
            checkIn.carKmPerMonth <= 500 -> 0.78
            checkIn.carKmPerMonth <= 900 -> 0.95
            checkIn.carKmPerMonth <= 1400 -> 1.08
            else -> 1.18
        }
        val flightAdjustment = (checkIn?.flightsPerYear ?: 0) * 180.0

        val streakBonus = if (selectedTips.contains("transport_tip")) (currentStreak * 0.005).coerceAtMost(0.03) else 0.0
        val tipReduction = if (selectedTips.contains("transport_tip")) 0.08 else 0.0
        val learningReduction = lessonReduction(completedLessons, "transport", perLesson = 0.012, maxReduction = 0.12)
        return reduced((base * kmAdjustment) + flightAdjustment, tipReduction + streakBonus + learningReduction)
    }

    private fun calculateFood(
        answers: OnboardingAnswers?,
        selectedTips: Set<String>,
        completedLessons: Set<String>,
        currentStreak: Int
    ): Double {
        val diet = answers?.diet.orEmpty()
        val base = when {
            diet.contains("Mainly veggie") -> 1450.0
            diet.contains("Pescatarian") -> 1750.0
            diet.contains("No red meat") -> 2100.0
            else -> 2750.0
        }
        val streakBonus = if (selectedTips.contains("food_tip")) (currentStreak * 0.004).coerceAtMost(0.025) else 0.0
        val tipReduction = if (selectedTips.contains("food_tip")) 0.07 else 0.0
        val learningReduction = lessonReduction(completedLessons, "food", perLesson = 0.01, maxReduction = 0.1)
        return reduced(base, tipReduction + streakBonus + learningReduction)
    }

    private fun calculateHome(
        answers: OnboardingAnswers?,
        checkIn: CarbonCheckIn?,
        selectedTips: Set<String>,
        completedLessons: Set<String>,
        currentStreak: Int
    ): Double {
        val people = ((answers?.peopleExcludingSelf ?: 0) + 1).coerceAtLeast(1)
        val buildingBase = when (answers?.buildingType) {
            "House" -> 4200.0
            "Townhouse" -> 3000.0
            else -> 2200.0
        }
        val bedroomsAdjustment = (answers?.bedrooms ?: 1).coerceAtLeast(1) * 260.0
        val perPerson = (buildingBase + bedroomsAdjustment) / people
        val tipReduction = buildList {
            if (selectedTips.contains("energy_tip")) add(0.06)
            if (selectedTips.contains("water_tip")) add(0.04)
        }.sum()
        val streakBonus = buildList {
            if (selectedTips.contains("energy_tip")) add((currentStreak * 0.004).coerceAtMost(0.02))
            if (selectedTips.contains("water_tip")) add((currentStreak * 0.003).coerceAtMost(0.015))
        }.sum()
        val learningReduction = lessonReduction(completedLessons, "home", perLesson = 0.01, maxReduction = 0.12)
        val deltaMultiplier = 1 + ((checkIn?.homeEnergyDeltaPercent ?: 0) / 100.0)
        return reduced(perPerson * deltaMultiplier.coerceIn(0.65, 1.45), tipReduction + streakBonus + learningReduction)
    }

    private fun calculatePurchases(
        checkIn: CarbonCheckIn?,
        selectedTips: Set<String>,
        completedLessons: Set<String>,
        currentStreak: Int
    ): Double {
        val base = 1850.0
        val streakBonus = if (selectedTips.contains("shopping_tip")) (currentStreak * 0.005).coerceAtMost(0.03) else 0.0
        val tipReduction = if (selectedTips.contains("shopping_tip")) 0.09 else 0.0
        val learningReduction = lessonReduction(completedLessons, "purchases", perLesson = 0.012, maxReduction = 0.12)
        val deltaMultiplier = 1 + ((checkIn?.shoppingDeltaPercent ?: 0) / 100.0)
        return reduced(base * deltaMultiplier.coerceIn(0.65, 1.5), tipReduction + streakBonus + learningReduction)
    }

    private fun calculateServices(
        selectedTips: Set<String>,
        completedLessons: Set<String>,
        currentStreak: Int
    ): Double {
        val base = 980.0
        val crossCategoryBonus = buildList {
            if (selectedTips.contains("energy_tip")) add(0.02)
            if (selectedTips.contains("shopping_tip")) add(0.02)
            if (selectedTips.contains("water_tip")) add(0.015)
        }.sum()
        val streakBonus = if (selectedTips.isNotEmpty()) (currentStreak * 0.002).coerceAtMost(0.02) else 0.0
        val learningCount = completedLessons.size
        val learningReduction = (learningCount * 0.0025).coerceAtMost(0.05)
        return reduced(base, crossCategoryBonus + streakBonus + learningReduction)
    }

    private fun calculateNatureOffset(
        selectedTips: Set<String>,
        completedLessons: Set<String>,
        currentStreak: Int
    ): Double {
        val tipBonus = if (selectedTips.contains("trees_tip")) 180.0 else 0.0
        val streakBonus = if (selectedTips.contains("trees_tip")) (currentStreak * 12.0).coerceAtMost(96.0) else 0.0
        val lessonsBonus = completedLessons.count { it.startsWith("trees_") } * 28.0
        return (tipBonus + streakBonus + lessonsBonus).coerceAtMost(520.0)
    }

    private fun lessonReduction(
        completedLessons: Set<String>,
        categoryId: String,
        perLesson: Double,
        maxReduction: Double
    ): Double {
        val count = completedLessons.count { it.startsWith("${categoryId}_") }
        return (count * perLesson).coerceAtMost(maxReduction)
    }

    private fun reduced(base: Double, reduction: Double): Double {
        val safeReduction = reduction.coerceIn(0.0, 0.35)
        return (base * (1.0 - safeReduction)).coerceAtLeast(0.0)
    }

    private fun resolveCountryAverageKg(country: String?): Double {
        return when (country?.trim()) {
            "United States" -> 14500.0
            "Canada" -> 15000.0
            "Germany" -> 8200.0
            "United Kingdom" -> 6700.0
            "France" -> 5100.0
            "Poland" -> 7900.0
            "Hungary" -> 5600.0
            "Ukraine" -> 4700.0
            "Italy" -> 5700.0
            "Spain" -> 5600.0
            "India" -> 2400.0
            else -> 8200.0
        }
    }

    private fun formatComparison(current: Double, baseline: Double): String {
        if (baseline <= 0.0) return "0% same"
        val percent = (((current - baseline) / baseline) * 100).toInt()
        return when {
            percent == 0 -> "0% same"
            percent > 0 -> "${abs(percent)}% more"
            else -> "${abs(percent)}% less"
        }
    }

    private fun buildRecommendations(
        answers: OnboardingAnswers?,
        categories: List<CarbonCategoryScore>,
        selectedTips: Set<String>,
        completedLessons: Set<String>
    ): List<CarbonRecommendation> {
        val output = mutableListOf<CarbonRecommendation>()

        val highest = categories.sortedByDescending { it.kgPerYear }.take(2)
        highest.forEach { category ->
            when (category.id) {
                "transport" -> {
                    if (!selectedTips.contains("transport_tip")) {
                        output += CarbonRecommendation(
                            "Complete the Transport tip",
                            "Your transport footprint is the largest. Start with the transport personal tip to unlock the fastest first reduction."
                        )
                    } else if (completedLessons.count { it.startsWith("transport_") } < 3) {
                        output += CarbonRecommendation(
                            "Continue Transport learning",
                            "You already started transport action. Completing a few transport lessons will improve future recommendations and lower your transport estimate."
                        )
                    }
                }
                "food" -> {
                    if (!selectedTips.contains("food_tip")) {
                        output += CarbonRecommendation(
                            "Reduce food impact",
                            "Food is one of your highest categories. Completing the food personal tip is the quickest next step."
                        )
                    } else {
                        output += CarbonRecommendation(
                            "Strengthen your food habits",
                            "Keep building lower-impact meals and reduce waste to push your food category down further."
                        )
                    }
                }
                "home" -> {
                    if (!selectedTips.contains("energy_tip")) {
                        output += CarbonRecommendation(
                            "Start with home energy",
                            "Home emissions are significant. The energy personal tip is the clearest next action for this category."
                        )
                    } else if (!selectedTips.contains("water_tip")) {
                        output += CarbonRecommendation(
                            "Add water efficiency",
                            "You already addressed energy. Add the water personal tip to reduce home resource use further."
                        )
                    }
                }
                "purchases" -> {
                    if (!selectedTips.contains("shopping_tip")) {
                        output += CarbonRecommendation(
                            "Lower purchase emissions",
                            "Purchases are driving part of your footprint. The shopping personal tip is the best first move here."
                        )
                    }
                }
            }
        }

        if (!selectedTips.contains("trees_tip")) {
            output += CarbonRecommendation(
                "Add a nature offset",
                "Completing the Trees tip increases your positive nature offset and unlocks nature-related achievements."
            )
        }

        if (answers?.goals?.contains("Cut carbon") == true) {
            output += CarbonRecommendation(
                "Track monthly changes next",
                "You selected carbon reduction as a goal. The next strong step is to add regular check-ins so estimates can become closer to real usage."
            )
        }

        return output.distinctBy { it.title }.take(3)
    }
}

object CarbonTrackerStore {
    private const val PREFS_NAME = "carbon_tracker_state"
    private const val KEY_LATEST_CHECKIN = "latest_checkin"
    private const val KEY_HISTORY = "history"

    fun currentMonthKey(): String = SimpleDateFormat("yyyy-MM", Locale.US).format(Date())

    fun getLatestCheckIn(context: Context): CarbonCheckIn? {
        val raw = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LATEST_CHECKIN, null)
            ?: return null
        return try {
            val json = JSONObject(raw)
            CarbonCheckIn(
                monthKey = json.optString("monthKey"),
                carKmPerMonth = json.optInt("carKmPerMonth"),
                flightsPerYear = json.optInt("flightsPerYear"),
                homeEnergyDeltaPercent = json.optInt("homeEnergyDeltaPercent"),
                shoppingDeltaPercent = json.optInt("shoppingDeltaPercent"),
                savedAtMs = json.optLong("savedAtMs")
            )
        } catch (_: Exception) {
            null
        }
    }

    fun saveCheckIn(context: Context, checkIn: CarbonCheckIn, snapshot: CarbonTrackerSnapshot) {
        persistLatestCheckIn(context, checkIn)
        val history = getHistory(context)
            .filterNot { it.monthKey == checkIn.monthKey }
            .toMutableList()
            .apply { add(CarbonHistoryEntry(checkIn.monthKey, snapshot.totalKgPerYear, checkIn.savedAtMs)) }
            .sortedByDescending { it.createdAtMs }
            .take(12)

        persistHistory(context, history)
    }

    fun setLatestCheckIn(context: Context, checkIn: CarbonCheckIn) {
        persistLatestCheckIn(context, checkIn)
    }

    fun clearLatestCheckIn(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_LATEST_CHECKIN)
            .apply()
    }

    fun setHistory(context: Context, history: List<CarbonHistoryEntry>) {
        persistHistory(context, history)
    }

    private fun persistLatestCheckIn(context: Context, checkIn: CarbonCheckIn) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LATEST_CHECKIN, JSONObject().apply {
                put("monthKey", checkIn.monthKey)
                put("carKmPerMonth", checkIn.carKmPerMonth)
                put("flightsPerYear", checkIn.flightsPerYear)
                put("homeEnergyDeltaPercent", checkIn.homeEnergyDeltaPercent)
                put("shoppingDeltaPercent", checkIn.shoppingDeltaPercent)
                put("savedAtMs", checkIn.savedAtMs)
            }.toString())
            .apply()
    }

    fun getHistory(context: Context): List<CarbonHistoryEntry> {
        val raw = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_HISTORY, null)
            ?: return emptyList()
        return try {
            val array = JSONArray(raw)
            buildList {
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)
                    add(
                        CarbonHistoryEntry(
                            monthKey = item.optString("monthKey"),
                            totalKgPerYear = item.optDouble("totalKgPerYear"),
                            createdAtMs = item.optLong("createdAtMs")
                        )
                    )
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun persistHistory(context: Context, history: List<CarbonHistoryEntry>) {
        val array = JSONArray()
        history.forEach { item ->
            array.put(JSONObject().apply {
                put("monthKey", item.monthKey)
                put("totalKgPerYear", item.totalKgPerYear)
                put("createdAtMs", item.createdAtMs)
            })
        }
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_HISTORY, array.toString())
            .apply()
    }
}
