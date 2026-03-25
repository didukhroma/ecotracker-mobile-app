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
    val totalRewards = 30

    fun getItems(): List<AchievementItem> {
        return listOf(
            AchievementItem("eco_starter", "Eco Starter", "Welcome to your eco-journey! You've taken the first step toward reducing your carbon footprint.", "screen_2.png", listOf("Complete your registration", "Complete the carbon footprint survey"), listOf("'Eco Starter' badge", "10 points for tips and challenges")),
            AchievementItem("green_routine", "Green Routine", "You built daily green habits for one week.", "screen_3.png", listOf("Open the app 7 days in a row", "Complete 3 tips"), listOf("'Green Routine' badge", "15 points for challenges")),
            AchievementItem("habit_hero", "Habit Hero", "You consistently apply eco-friendly habits.", "screen_4.png", listOf("Complete 10 tips", "Finish 3 learning lessons"), listOf("'Habit Hero' badge", "20 points for challenges")),
            AchievementItem("carpool_champ", "Carpool Champ", "You replaced solo trips with shared rides.", "question.png", listOf("Read Transport Lesson 2", "Complete 5 shared rides"), listOf("'Carpool Champ' badge", "25 transport points")),
            AchievementItem("pedal_power", "Pedal Power", "You logged bike rides and reduced car trips.", "greeting.png", listOf("Complete 50 bike rides", "Save at least 100 kg of CO2 emissions"), listOf("'Pedal Power' badge", "30 mobility points")),
            AchievementItem("eco_commuter", "Eco Commuter", "You optimized your commute for lower emissions.", "loading.png", listOf("Use public transport 10 times", "Complete Transport category"), listOf("'Eco Commuter' badge", "20 mobility points")),
            AchievementItem("energy_saver", "Energy Saver", "You improved home energy efficiency.", "forgot.png", listOf("Complete 3 home tips", "Reduce monthly energy use by 5%"), listOf("'Energy Saver' badge", "15 home points")),
            AchievementItem("off_the_grid", "Off the Grid", "You actively lowered household dependence on high-energy habits.", "home.png", listOf("Complete Home category", "Mark 5 personal tips as done"), listOf("'Off the Grid' badge", "25 home points")),
            AchievementItem("eco_illuminator", "Eco Illuminator", "You switched to efficient lighting and reduced usage.", "screen_5.png", listOf("Finish Home Lesson 2", "Replace 5 bulbs with LEDs"), listOf("'Eco Illuminator' badge", "10 home points")),
            AchievementItem("carbon_cutter", "Carbon Cutter", "You achieved measurable emissions reduction.", "screen_2.png", listOf("Reduce footprint by 10%", "Complete at least 2 categories"), listOf("'Carbon Cutter' badge", "35 impact points")),
            AchievementItem("halfway_hero", "Halfway Hero", "You are halfway through your impact journey.", "screen_3.png", listOf("Claim 5 achievements", "Complete 5 lessons"), listOf("'Halfway Hero' badge", "20 impact points")),
            AchievementItem("carbon_neutral", "Carbon Neutral", "You balanced emissions through long-term action.", "screen_4.png", listOf("Complete all onboarding goals", "Claim 10 achievements"), listOf("'Carbon Neutral' badge", "50 impact points"))
        )
    }

    fun getState(context: Context, item: AchievementItem): AchievementState {
        val unlocked = computeUnlocked(context, item.id)
        val claimed = AchievementStore.isClaimed(context, item.id)
        return AchievementState(item, unlocked, claimed)
    }

    fun getStates(context: Context): List<AchievementState> = getItems().map { getState(context, it) }

    private fun computeUnlocked(context: Context, achievementId: String): Boolean {
        return when (achievementId) {
            "eco_starter" -> hasOnboardingData()
            "green_routine" -> LearningRepository.getCategories().isNotEmpty()
            "habit_hero" -> LearningProgressStore.getTotalCompleted(context) >= 1
            "carpool_champ" -> LearningProgressStore.isCompleted(context, "transport_2")
            "pedal_power" -> AchievementStore.isClaimed(context, "carpool_champ")
            "eco_commuter" -> LearningProgressStore.getCategoryCompleted(context, "transport") >= 2
            "energy_saver" -> LearningProgressStore.getCategoryCompleted(context, "home") >= 1
            "off_the_grid" -> LearningProgressStore.getCategoryCompleted(context, "home") >= 3
            "eco_illuminator" -> LearningProgressStore.isCompleted(context, "home_2")
            "carbon_cutter" -> AchievementStore.claimedCount(context) >= 3
            "halfway_hero" -> AchievementStore.claimedCount(context) >= 5
            "carbon_neutral" -> AchievementStore.claimedCount(context) >= 10
            else -> false
        }
    }

    private fun hasOnboardingData(): Boolean = !OnboardingSessionStore.latestPayload.isNullOrBlank()
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
