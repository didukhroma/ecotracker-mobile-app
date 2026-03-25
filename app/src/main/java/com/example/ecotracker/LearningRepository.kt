package com.example.ecotracker

import android.content.Context

data class LearningCategory(
    val id: String,
    val title: String,
    val lessons: List<LearningLesson>
)

data class LearningLesson(
    val id: String,
    val categoryId: String,
    val title: String,
    val shortDescription: String,
    val content: String,
    val imageAsset: String
)

object LearningRepository {
    fun getCategories(): List<LearningCategory> {
        return listOf(
            LearningCategory(
                id = "transport",
                title = "Transport",
                lessons = listOf(
                    LearningLesson(
                        id = "transport_1",
                        categoryId = "transport",
                        title = "The Power of Public Transport",
                        shortDescription = "Switch to buses or trains to cut emissions and simplify travel.",
                        content = "Public transport reduces per-person emissions, traffic congestion, and fuel costs. Start by replacing one weekly car commute with bus or metro trips.",
                        imageAsset = "screen_2.png"
                    ),
                    LearningLesson(
                        id = "transport_2",
                        categoryId = "transport",
                        title = "Carpooling for Carbon Savings",
                        shortDescription = "Share rides to lower emissions and travel costs.",
                        content = "Carpooling means fewer cars on roads and less fuel burned per person. Coordinate fixed weekly routes with colleagues or neighbors for easier planning.",
                        imageAsset = "screen_3.png"
                    ),
                    LearningLesson(
                        id = "transport_3",
                        categoryId = "transport",
                        title = "Embrace Cycling and Walking",
                        shortDescription = "Replace short trips with active travel.",
                        content = "Walking and cycling are near-zero emission for daily trips. Keep a 2-5 km route list and choose active transport for shopping and errands.",
                        imageAsset = "question.png"
                    )
                )
            ),
            LearningCategory(
                id = "food",
                title = "Food",
                lessons = listOf(
                    LearningLesson(
                        id = "food_1",
                        categoryId = "food",
                        title = "Eat More Seasonal Foods",
                        shortDescription = "Seasonal foods reduce transport and storage emissions.",
                        content = "Buying seasonal produce from local markets lowers energy use from long-distance logistics and refrigeration.",
                        imageAsset = "home.png"
                    ),
                    LearningLesson(
                        id = "food_2",
                        categoryId = "food",
                        title = "Reduce Food Waste",
                        shortDescription = "Plan meals and store food correctly to waste less.",
                        content = "Food waste means wasted resources and extra methane emissions. Weekly planning and proper storage can significantly cut losses.",
                        imageAsset = "screen_4.png"
                    ),
                    LearningLesson(
                        id = "food_3",
                        categoryId = "food",
                        title = "Plant-Forward Plate",
                        shortDescription = "Add more plant-based meals each week.",
                        content = "Start with two meat-free days per week. Beans, grains, and vegetables can provide balanced nutrition at lower carbon intensity.",
                        imageAsset = "screen_5.png"
                    )
                )
            ),
            LearningCategory(
                id = "home",
                title = "Home",
                lessons = listOf(
                    LearningLesson(
                        id = "home_1",
                        categoryId = "home",
                        title = "Smart Heating Basics",
                        shortDescription = "Optimize temperature settings for comfort and efficiency.",
                        content = "A small reduction in heating setpoint can save notable energy over a season. Use timers and zone heating where possible.",
                        imageAsset = "loading.png"
                    ),
                    LearningLesson(
                        id = "home_2",
                        categoryId = "home",
                        title = "Efficient Lighting Upgrade",
                        shortDescription = "Switch to LEDs and use task lighting.",
                        content = "LED lamps consume less power and last longer. Prioritize frequently used rooms first for the highest impact.",
                        imageAsset = "forgot.png"
                    ),
                    LearningLesson(
                        id = "home_3",
                        categoryId = "home",
                        title = "Water Saving at Home",
                        shortDescription = "Install aerators and fix leaks early.",
                        content = "Small leaks add up quickly. Low-flow fixtures reduce water use and heating demand for hot water.",
                        imageAsset = "greeting.png"
                    )
                )
            ),
            LearningCategory(
                id = "purchases",
                title = "Purchases",
                lessons = listOf(
                    LearningLesson(
                        id = "purchases_1",
                        categoryId = "purchases",
                        title = "Buy Durable Products",
                        shortDescription = "Choose quality over quantity for long-term impact.",
                        content = "Durable items reduce replacement frequency and embedded emissions from production and shipping.",
                        imageAsset = "home.png"
                    ),
                    LearningLesson(
                        id = "purchases_2",
                        categoryId = "purchases",
                        title = "Repair Before Replacing",
                        shortDescription = "Extend product life through basic repairs.",
                        content = "Repairing electronics and appliances can prevent early disposal and reduce demand for new manufacturing.",
                        imageAsset = "screen_2.png"
                    ),
                    LearningLesson(
                        id = "purchases_3",
                        categoryId = "purchases",
                        title = "Choose Refill and Reuse",
                        shortDescription = "Avoid single-use packaging when possible.",
                        content = "Refill systems and reusable containers reduce waste and packaging emissions over time.",
                        imageAsset = "screen_3.png"
                    )
                )
            )
        )
    }

    fun getCategory(categoryId: String): LearningCategory? {
        return getCategories().firstOrNull { it.id == categoryId }
    }

    fun getLesson(lessonId: String): LearningLesson? {
        return getCategories().flatMap { it.lessons }.firstOrNull { it.id == lessonId }
    }
}

object LearningProgressStore {
    private const val PREFS_NAME = "learning_progress"
    private const val KEY_COMPLETED_IDS = "completed_ids"

    fun isCompleted(context: Context, lessonId: String): Boolean {
        return getCompletedIds(context).contains(lessonId)
    }

    fun markCompleted(context: Context, lessonId: String) {
        val updated = getCompletedIds(context).toMutableSet().apply { add(lessonId) }
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(KEY_COMPLETED_IDS, updated)
            .apply()
    }

    fun completedCount(context: Context, category: LearningCategory): Int {
        val completed = getCompletedIds(context)
        return category.lessons.count { completed.contains(it.id) }
    }

    fun getCategoryCompleted(context: Context, categoryId: String): Int {
        val category = LearningRepository.getCategory(categoryId) ?: return 0
        return completedCount(context, category)
    }

    fun getTotalCompleted(context: Context): Int = getCompletedIds(context).size

    private fun getCompletedIds(context: Context): Set<String> {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getStringSet(KEY_COMPLETED_IDS, emptySet())
            ?.toSet()
            ?: emptySet()
    }
}
