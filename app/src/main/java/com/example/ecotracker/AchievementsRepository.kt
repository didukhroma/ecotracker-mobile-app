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
                requirements = listOf("Mark any 1 personal tip as completed"),
            ),
            achievement(
                "green_routine",
                "Green Routine",
                "Build a steady eco routine by following several personal tips across categories.",
                requirements = listOf("Mark any 3 personal tips as completed"),
            ),
            achievement(
                "habit_hero",
                "Habit Hero",
                "Show full consistency by completing every available personal tip category.",
                requirements = listOf("Mark all 6 personal tips as completed"),
            ),
            achievement(
                "carpool_champ",
                "Carpool Champ",
                "Transport category achievement for taking action on lower-impact mobility habits.",
                requirements = listOf("Complete the Transport personal tip"),
            ),
            achievement(
                "pedal_power",
                "Pedal Power",
                "Transport category achievement for starting your learning journey in sustainable mobility.",
                requirements = listOf("Complete at least 1 Transport lesson"),
            ),
            achievement(
                "eco_commuter",
                "Eco Commuter",
                "Transport category achievement for building strong sustainable commuting knowledge.",
                requirements = listOf("Complete at least 5 Transport lessons"),
            ),
            achievement(
                "energy_saver",
                "Energy Saver",
                "Energy category achievement for applying a direct household energy-saving tip.",
                requirements = listOf("Complete the Energy consumption personal tip"),
            ),
            achievement(
                "eco_illuminator",
                "Eco Illuminator",
                "Energy category achievement for starting to learn cleaner and more efficient home habits.",
                requirements = listOf("Complete at least 1 Home lesson"),
            ),
            achievement(
                "power_wise",
                "Power Wise",
                "Energy category achievement for building deeper knowledge in home efficiency and energy use.",
                requirements = listOf("Complete at least 5 Home lessons"),
            ),
            achievement(
                "carbon_cutter",
                "Carbon Cutter",
                "Carbon footprint achievement for reducing impact across multiple areas of daily life.",
                requirements = listOf("Complete any 2 personal tips"),
            ),
            achievement(
                "carbon_neutral",
                "Carbon Neutral",
                "Carbon footprint achievement for showing broad and consistent eco progress.",
                requirements = listOf("Complete at least 10 learning lessons"),
            ),
            achievement(
                "low_impact",
                "Low Impact",
                "Carbon footprint achievement for advanced progress across learning and habits.",
                requirements = listOf("Complete at least 20 learning lessons"),
            ),
            achievement(
                "off_the_grid",
                "Off the Grid",
                "Lifestyle category achievement for taking practical steps toward lower-consumption habits.",
                requirements = listOf("Complete the Shopping personal tip"),
            ),
            achievement(
                "halfway_hero",
                "Halfway Hero",
                "Lifestyle category achievement for reaching an early milestone in the learning platform.",
                requirements = listOf("Complete at least 5 learning lessons"),
            ),
            achievement(
                "green_guardian",
                "Green Guardian",
                "Lifestyle category achievement for combining personal actions with strong learning progress.",
                requirements = listOf("Complete at least 5 personal tips", "Complete at least 15 learning lessons"),
            ),
            achievement(
                "tree_planter",
                "Tree Planter",
                "Trees category achievement for taking your first direct nature-positive action.",
                requirements = listOf("Complete the Trees personal tip"),
            ),
            achievement(
                "forest_friend",
                "Forest Friend",
                "Trees category achievement for beginning your learning progress on forests and biodiversity.",
                requirements = listOf("Complete at least 1 Trees lesson"),
            ),
            achievement(
                "earth_protector",
                "Earth Protector",
                "Trees category achievement for sustained progress in nature and forest protection learning.",
                requirements = listOf("Complete at least 5 Trees lessons"),
            ),
            achievement(
                "eco_shopper",
                "Eco Shopper",
                "Conscious consumption achievement for learning how purchases affect sustainability.",
                requirements = listOf("Complete at least 1 Purchases lesson"),
            ),
            achievement(
                "waste_warrior",
                "Waste Warrior",
                "Conscious consumption achievement for developing stronger low-waste shopping knowledge.",
                requirements = listOf("Complete at least 5 Purchases lessons"),
            ),
            achievement(
                "reuse_master",
                "Reuse Master",
                "Conscious consumption achievement for combining practical shopping actions with reuse learning.",
                requirements = listOf("Complete the Shopping personal tip", "Complete at least 3 Purchases lessons"),
            ),
            achievement(
                "water_saver",
                "Water Saver",
                "Resources category achievement for applying a direct water-saving action at home.",
                requirements = listOf("Complete the Water personal tip"),
            ),
            achievement(
                "resource_keeper",
                "Resource Keeper",
                "Resources category achievement for building stronger knowledge of efficient home resource use.",
                requirements = listOf("Complete at least 3 Home lessons"),
            ),
            achievement(
                "planet_protector",
                "Planet Protector",
                "Top-tier achievement for combining all personal tip categories with strong learning progress.",
                requirements = listOf("Complete all 6 personal tips", "Complete at least 25 learning lessons"),
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
        val selectedTips = PersonalTipsStore.getSelectedIds(context)
        val selectedTipsCount = selectedTips.size
        val completedLessons = LearningProgressStore.getCompletedIds(context)
        val completedLessonsCount = completedLessons.size

        fun categoryCount(categoryId: String): Int = LearningProgressStore.getCategoryCompleted(context, categoryId)
        fun hasTip(tipId: String): Boolean = selectedTips.contains(tipId)

        return when (achievementId) {
            "eco_starter" -> selectedTipsCount >= 1
            "green_routine" -> selectedTipsCount >= 3
            "habit_hero" -> selectedTipsCount >= 6

            "carpool_champ" -> hasTip("transport_tip")
            "pedal_power" -> categoryCount("transport") >= 1
            "eco_commuter" -> categoryCount("transport") >= 5

            "energy_saver" -> hasTip("energy_tip")
            "eco_illuminator" -> categoryCount("home") >= 1
            "power_wise" -> categoryCount("home") >= 5

            "carbon_cutter" -> selectedTipsCount >= 2
            "carbon_neutral" -> completedLessonsCount >= 10
            "low_impact" -> completedLessonsCount >= 20

            "off_the_grid" -> hasTip("shopping_tip")
            "halfway_hero" -> completedLessonsCount >= 5
            "green_guardian" -> selectedTipsCount >= 5 && completedLessonsCount >= 15

            "tree_planter" -> hasTip("trees_tip")
            "forest_friend" -> categoryCount("trees") >= 1
            "earth_protector" -> categoryCount("trees") >= 5

            "eco_shopper" -> categoryCount("purchases") >= 1
            "waste_warrior" -> categoryCount("purchases") >= 5
            "reuse_master" -> hasTip("shopping_tip") && categoryCount("purchases") >= 3

            "water_saver" -> hasTip("water_tip")
            "resource_keeper" -> categoryCount("home") >= 3
            "planet_protector" -> selectedTipsCount >= 6 && completedLessonsCount >= 25

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
