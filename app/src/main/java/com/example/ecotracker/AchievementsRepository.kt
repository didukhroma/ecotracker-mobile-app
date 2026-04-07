package com.example.ecotracker

import android.content.Context

data class AchievementItem(
    val id: String,
    val title: String,
    val description: String,
    val imageAsset: String,
    val requirements: List<String>,
    val rewards: List<String>
)

data class AchievementState(
    val item: AchievementItem,
    val unlocked: Boolean,
    val claimed: Boolean
)

object AchievementsRepository {
    val totalRewards: Int
        get() = getItems().size

    fun getItems(): List<AchievementItem> {
        return listOf(
            achievement(
                "eco_starter",
                "Eco Starter",
                "Start your sustainability journey by completing your first personal eco tip.",
                requirements = listOf("Complete any 1 daily personal tip"),
            ),
            achievement(
                "green_routine",
                "Green Routine",
                "Build a steady eco routine by following several personal tips across categories.",
                requirements = listOf("Complete at least 7 daily personal tips in total", "Be active on at least 3 different days"),
            ),
            achievement(
                "habit_hero",
                "Habit Hero",
                "Show full consistency by completing every available personal tip category.",
                requirements = listOf("Complete all 6 personal tip categories at least once", "Reach a 5-day tips streak"),
            ),
            achievement(
                "carpool_champ",
                "Carpool Champ",
                "Transport category achievement for taking action on lower-impact mobility habits.",
                requirements = listOf("Complete the Transport personal tip on 3 different days"),
            ),
            achievement(
                "pedal_power",
                "Pedal Power",
                "Transport category achievement for starting your learning journey in sustainable mobility.",
                requirements = listOf("Complete at least 3 Transport lessons"),
            ),
            achievement(
                "eco_commuter",
                "Eco Commuter",
                "Transport category achievement for building strong sustainable commuting knowledge.",
                requirements = listOf("Complete at least 8 Transport lessons", "Complete the Transport personal tip on 5 different days"),
            ),
            achievement(
                "energy_saver",
                "Energy Saver",
                "Energy category achievement for applying a direct household energy-saving tip.",
                requirements = listOf("Complete the Home energy personal tip on 3 different days"),
            ),
            achievement(
                "eco_illuminator",
                "Eco Illuminator",
                "Energy category achievement for starting to learn cleaner and more efficient home habits.",
                requirements = listOf("Complete at least 3 Home lessons"),
            ),
            achievement(
                "power_wise",
                "Power Wise",
                "Energy category achievement for building deeper knowledge in home efficiency and energy use.",
                requirements = listOf("Complete at least 8 Home lessons", "Complete the Home energy personal tip on 5 different days"),
            ),
            achievement(
                "carbon_cutter",
                "Carbon Cutter",
                "Carbon footprint achievement for reducing impact across multiple areas of daily life.",
                requirements = listOf("Complete at least 10 daily personal tips in total", "Reach a 3-day tips streak"),
            ),
            achievement(
                "carbon_neutral",
                "Carbon Neutral",
                "Carbon footprint achievement for showing broad and consistent eco progress.",
                requirements = listOf("Complete at least 20 learning lessons", "Complete at least 4 personal tip categories"),
            ),
            achievement(
                "low_impact",
                "Low Impact",
                "Carbon footprint achievement for advanced progress across learning and habits.",
                requirements = listOf("Complete at least 35 learning lessons", "Reach a best tips streak of 10 days"),
            ),
            achievement(
                "off_the_grid",
                "Off the Grid",
                "Lifestyle category achievement for taking practical steps toward lower-consumption habits.",
                requirements = listOf("Complete the Shopping personal tip on 3 different days"),
            ),
            achievement(
                "halfway_hero",
                "Halfway Hero",
                "Lifestyle category achievement for reaching an early milestone in the learning platform.",
                requirements = listOf("Complete at least 12 learning lessons"),
            ),
            achievement(
                "green_guardian",
                "Green Guardian",
                "Lifestyle category achievement for combining personal actions with strong learning progress.",
                requirements = listOf("Complete at least 20 daily personal tips in total", "Complete at least 25 learning lessons", "Reach a best tips streak of 7 days"),
            ),
            achievement(
                "tree_planter",
                "Tree Planter",
                "Trees category achievement for taking your first direct nature-positive action.",
                requirements = listOf("Complete the Trees personal tip on 3 different days"),
            ),
            achievement(
                "forest_friend",
                "Forest Friend",
                "Trees category achievement for beginning your learning progress on forests and biodiversity.",
                requirements = listOf("Complete at least 3 Trees lessons"),
            ),
            achievement(
                "earth_protector",
                "Earth Protector",
                "Trees category achievement for sustained progress in nature and forest protection learning.",
                requirements = listOf("Complete at least 8 Trees lessons", "Complete the Trees personal tip on 5 different days"),
            ),
            achievement(
                "eco_shopper",
                "Eco Shopper",
                "Conscious consumption achievement for learning how purchases affect sustainability.",
                requirements = listOf("Complete at least 3 Purchases lessons"),
            ),
            achievement(
                "waste_warrior",
                "Waste Warrior",
                "Conscious consumption achievement for developing stronger low-waste shopping knowledge.",
                requirements = listOf("Complete at least 8 Purchases lessons", "Complete the Shopping personal tip on 5 different days"),
            ),
            achievement(
                "reuse_master",
                "Reuse Master",
                "Conscious consumption achievement for combining practical shopping actions with reuse learning.",
                requirements = listOf("Complete the Shopping personal tip on 5 different days", "Complete at least 5 Purchases lessons"),
            ),
            achievement(
                "water_saver",
                "Water Saver",
                "Resources category achievement for applying a direct water-saving action at home.",
                requirements = listOf("Complete the Home water personal tip on 3 different days"),
            ),
            achievement(
                "resource_keeper",
                "Resource Keeper",
                "Resources category achievement for building stronger knowledge of efficient home resource use.",
                requirements = listOf("Complete at least 6 Home lessons", "Complete the Home water personal tip on 5 different days"),
            ),
            achievement(
                "planet_protector",
                "Planet Protector",
                "Top-tier achievement for combining all personal tip categories with strong learning progress.",
                requirements = listOf("Complete all 6 personal tip categories at least once", "Complete at least 40 learning lessons", "Reach a best tips streak of 14 days", "Be active on at least 14 different tip days"),
            )
        )
    }

    fun getState(context: Context, item: AchievementItem): AchievementState {
        val unlocked = computeUnlocked(context, item.id)
        val claimed = AchievementStore.isClaimed(context, item.id)
        return AchievementState(item, unlocked, claimed)
    }

    fun getStates(context: Context): List<AchievementState> = getItems().map { getState(context, it) }

    private fun computeUnlocked(context: Context, achievementId: String): Boolean {
        val totalTipCompletions = PersonalTipsStore.totalCompletionEvents(context)
        val uniqueTipCategories = PersonalTipsStore.uniqueCompletedTipsCount(context)
        val activeDays = PersonalTipsStore.activeDaysCount(context)
        val currentStreak = PersonalTipsStore.currentStreak(context)
        val bestStreak = PersonalTipsStore.bestStreak(context)
        val completedLessons = LearningProgressStore.getCompletedIds(context)
        val completedLessonsCount = completedLessons.size

        fun categoryCount(categoryId: String): Int = LearningProgressStore.getCategoryCompleted(context, categoryId)
        fun hasTip(tipId: String): Boolean = PersonalTipsStore.hasEverCompleted(context, tipId)
        fun tipDays(tipId: String): Int = PersonalTipsStore.completionCountForTip(context, tipId)

        return when (achievementId) {
            "eco_starter" -> totalTipCompletions >= 1
            "green_routine" -> totalTipCompletions >= 7 && activeDays >= 3
            "habit_hero" -> uniqueTipCategories >= 6 && bestStreak >= 5

            "carpool_champ" -> tipDays("transport_tip") >= 3
            "pedal_power" -> categoryCount("transport") >= 3
            "eco_commuter" -> categoryCount("transport") >= 8 && tipDays("transport_tip") >= 5

            "energy_saver" -> tipDays("energy_tip") >= 3
            "eco_illuminator" -> categoryCount("home") >= 3
            "power_wise" -> categoryCount("home") >= 8 && tipDays("energy_tip") >= 5

            "carbon_cutter" -> totalTipCompletions >= 10 && currentStreak >= 3
            "carbon_neutral" -> completedLessonsCount >= 20 && uniqueTipCategories >= 4
            "low_impact" -> completedLessonsCount >= 35 && bestStreak >= 10

            "off_the_grid" -> tipDays("shopping_tip") >= 3
            "halfway_hero" -> completedLessonsCount >= 12
            "green_guardian" -> totalTipCompletions >= 20 && completedLessonsCount >= 25 && bestStreak >= 7

            "tree_planter" -> tipDays("trees_tip") >= 3
            "forest_friend" -> categoryCount("trees") >= 3
            "earth_protector" -> categoryCount("trees") >= 8 && tipDays("trees_tip") >= 5

            "eco_shopper" -> categoryCount("purchases") >= 3
            "waste_warrior" -> categoryCount("purchases") >= 8 && tipDays("shopping_tip") >= 5
            "reuse_master" -> tipDays("shopping_tip") >= 5 && categoryCount("purchases") >= 5

            "water_saver" -> tipDays("water_tip") >= 3
            "resource_keeper" -> categoryCount("home") >= 6 && tipDays("water_tip") >= 5
            "planet_protector" -> uniqueTipCategories >= 6 && completedLessonsCount >= 40 && bestStreak >= 14 && activeDays >= 14

            else -> false
        }
    }

    private fun achievement(
        id: String,
        title: String,
        description: String,
        requirements: List<String>,
        rewards: List<String> = listOf("'$title' badge", "10 points for tips and challenges")
    ): AchievementItem {
        return AchievementItem(
            id = id,
            title = title,
            description = description,
            imageAsset = "achievements/${id.replace('_', '-')}.jpg",
            requirements = requirements,
            rewards = rewards
        )
    }
}

object AchievementStore {
    private const val PREFS_NAME = "achievement_state"
    private const val KEY_CLAIMED_IDS = "claimed_ids"

    fun isClaimed(context: Context, id: String): Boolean = claimedIds(context).contains(id)

    fun getClaimedIds(context: Context): Set<String> = claimedIds(context)

    fun markClaimed(context: Context, id: String) {
        val set = claimedIds(context).toMutableSet().apply { add(id) }
        persistClaimedIds(context, set)
        FirebaseSync.syncAchievements(context)
    }

    fun setClaimedIds(context: Context, ids: Set<String>) {
        persistClaimedIds(context, ids)
    }

    fun claimedCount(context: Context): Int = claimedIds(context).size

    private fun claimedIds(context: Context): Set<String> {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getStringSet(KEY_CLAIMED_IDS, emptySet())
            ?.toSet()
            ?: emptySet()
    }

    private fun persistClaimedIds(context: Context, ids: Set<String>) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(KEY_CLAIMED_IDS, ids)
            .apply()
    }
}
