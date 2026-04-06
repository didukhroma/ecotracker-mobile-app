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
            // Basics
            item("eco_starter", "Eco Starter"),
            item("green_routine", "Green Routine"),
            item("habit_hero", "Habit Hero"),
            // Transport
            item("carpool_champ", "Carpool Champ"),
            item("pedal_power", "Pedal Power"),
            item("eco_commuter", "Eco Commuter"),
            // Energy
            item("energy_saver", "Energy Saver"),
            item("eco_illuminator", "Eco Illuminator"),
            item("power_wise", "Power Wise"),
            // Carbon footprint
            item("carbon_cutter", "Carbon Cutter"),
            item("carbon_neutral", "Carbon Neutral"),
            item("low_impact", "Low Impact"),
            // Lifestyle
            item("off_the_grid", "Off the Grid"),
            item("halfway_hero", "Halfway Hero"),
            item("green_guardian", "Green Guardian"),
            // Nature / Trees
            item("tree_planter", "Tree Planter"),
            item("forest_friend", "Forest Friend"),
            item("earth_protector", "Earth Protector"),
            // Conscious consumption
            item("eco_shopper", "Eco Shopper"),
            item("waste_warrior", "Waste Warrior"),
            item("reuse_master", "Reuse Master"),
            // Resources
            item("water_saver", "Water Saver"),
            item("resource_keeper", "Resource Keeper"),
            item("planet_protector", "Planet Protector")
        )
    }

    fun getState(context: Context, item: AchievementItem): AchievementState {
        val unlocked = computeUnlocked(context, item.id)
        val claimed = AchievementStore.isClaimed(context, item.id)
        return AchievementState(item, unlocked, claimed)
    }

    fun getStates(context: Context): List<AchievementState> = getItems().map { getState(context, it) }

    // Current stage request: keep achievements unopened/locked by default.
    private fun computeUnlocked(_context: Context, _achievementId: String): Boolean {
        return false
    }

    private fun item(id: String, title: String): AchievementItem {
        return AchievementItem(
            id = id,
            title = title,
            description = "This achievement is locked. Complete more eco actions to unlock it.",
            imageAsset = "achievements/${id.replace('_', '-')}.jpg",
            requirements = listOf(
                "Complete related eco activity",
                "Keep progress consistency"
            ),
            rewards = listOf(
                "'$title' badge",
                "10 points for tips and challenges"
            )
        )
    }
}

object AchievementStore {
    private const val PREFS_NAME = "achievement_state"
    private const val KEY_CLAIMED_IDS = "claimed_ids"

    fun isClaimed(context: Context, id: String): Boolean = claimedIds(context).contains(id)

    fun markClaimed(context: Context, id: String) {
        val set = claimedIds(context).toMutableSet().apply { add(id) }
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(KEY_CLAIMED_IDS, set)
            .apply()
    }

    fun claimedCount(context: Context): Int = claimedIds(context).size

    private fun claimedIds(context: Context): Set<String> {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getStringSet(KEY_CLAIMED_IDS, emptySet())
            ?.toSet()
            ?: emptySet()
    }
}
