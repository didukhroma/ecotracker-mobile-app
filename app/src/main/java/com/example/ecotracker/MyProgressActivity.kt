package com.example.ecotracker

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MyProgressActivity : AppCompatActivity() {

    private lateinit var data: ProgressData
    private lateinit var breakdownListContainer: LinearLayout
    private lateinit var personalTipsListContainer: LinearLayout
    private lateinit var learningListContainer: LinearLayout
    private lateinit var filterDropdown: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_progress)

        breakdownListContainer = findViewById(R.id.breakdownListContainer)
        personalTipsListContainer = findViewById(R.id.personalTipsListContainer)
        learningListContainer = findViewById(R.id.learningListContainer)
        filterDropdown = findViewById(R.id.breakdownFilterDropdown)

        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }

        FirebaseSync.fetchOnboardingAnswers(this) { answers ->
            data = MyProgressRepository.getProgressData(this, answers)
            bindTopSection()
            bindBreakdown()
            bindPersonalTips()
            bindLearningWay()
        }
    }

    private fun bindTopSection() {
        findViewById<TextView>(R.id.valueYou).text = String.format("%.1f", data.relativeEmissions.you)
        findViewById<TextView>(R.id.valueCitizen).text = String.format("%.1f", data.relativeEmissions.countryCitizen)
        findViewById<TextView>(R.id.valueGlobal).text = String.format("%.1f", data.relativeEmissions.global)
        findViewById<TextView>(R.id.citizenComparisonValue).text = data.comparisonVsCitizen
        findViewById<TextView>(R.id.globalComparisonValue).text = data.comparisonVsGlobal
        findViewById<TextView>(R.id.breakdownIntro).text = buildBreakdownIntro()

        renderStackedBar(findViewById(R.id.barYou), data.relativeEmissionsParts.you)
        renderStackedBar(findViewById(R.id.barCitizen), data.relativeEmissionsParts.countryCitizen)
        renderStackedBar(findViewById(R.id.barGlobal), data.relativeEmissionsParts.global)
    }

    private fun bindBreakdown() {
        val filterItems = data.breakdownFilters.map { it.label }
        filterDropdown.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_1, filterItems))
        filterDropdown.setText(filterItems.first(), false)
        filterDropdown.keyListener = null
        filterDropdown.setOnClickListener { filterDropdown.showDropDown() }
        filterDropdown.setOnItemClickListener { _, _, position, _ ->
            val rows = data.breakdownFilters.getOrNull(position)?.rows.orEmpty()
            renderBreakdownRows(rows)
        }
        renderBreakdownRows(data.breakdownFilters.firstOrNull()?.rows.orEmpty())
    }

    private fun renderBreakdownRows(rows: List<MetricRow>) {
        renderMetricRows(
            container = breakdownListContainer,
            rows = rows,
            unitLabel = getString(R.string.kg_unit)
        )
    }

    private fun bindPersonalTips() {
        findViewById<TextView>(R.id.personalTipsCount).text = data.personalTipsCount.toString()
        renderMetricRows(
            container = personalTipsListContainer,
            rows = data.personalTips,
            unitLabel = getString(R.string.tips_unit)
        )
    }

    private fun bindLearningWay() {
        findViewById<TextView>(R.id.learningCount).text = data.learningCount.toString()
        renderMetricRows(
            container = learningListContainer,
            rows = data.learning,
            unitLabel = getString(R.string.lessons_unit)
        )
    }

    private fun renderStackedBar(container: LinearLayout, values: List<Double>) {
        container.removeAllViews()
        val colors = listOf(
            R.color.chart_blue,
            R.color.chart_light_blue,
            R.color.chart_green,
            R.color.chart_yellow,
            R.color.chart_light_red,
            R.color.chart_red
        )
        val total = values.sum().takeIf { it > 0 } ?: 1.0
        values.forEachIndexed { index, value ->
            val weight = value.toFloat() / total.toFloat()
            val segment = View(this).apply {
                setBackgroundColor(ContextCompat.getColor(this@MyProgressActivity, colors[index % colors.size]))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    weight
                )
            }
            container.addView(segment)
        }
    }

    private fun renderMetricRows(
        container: LinearLayout,
        rows: List<MetricRow>,
        unitLabel: String
    ) {
        container.removeAllViews()

        rows.forEach { row ->
            val rowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).also { it.topMargin = dp(8) }
                gravity = Gravity.CENTER_VERTICAL
            }

            val label = TextView(this).apply {
                text = row.label
                setTextColor(ContextCompat.getColor(this@MyProgressActivity, R.color.lime_10))
                textSize = 12f
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.2f)
            }

            val barWrapper = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(0, dp(16), 2.2f).also { it.marginEnd = dp(8) }
                orientation = LinearLayout.HORIZONTAL
            }

            val barFill = View(this).apply {
                setBackgroundColor(ContextCompat.getColor(this@MyProgressActivity, R.color.lime_8))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (row.value / row.total.coerceAtLeast(1.0)).toFloat().coerceIn(0f, 1f)
                )
            }
            val barEmpty = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (1f - (row.value / row.total.coerceAtLeast(1.0)).toFloat().coerceIn(0f, 1f))
                )
            }
            barWrapper.addView(barFill)
            barWrapper.addView(barEmpty)

            val value = TextView(this).apply {
                text = if (unitLabel == getString(R.string.kg_unit)) {
                    String.format("%.0f %s", row.value, unitLabel)
                } else {
                    String.format("%.0f %s", row.value, unitLabel)
                }
                setTextColor(ContextCompat.getColor(this@MyProgressActivity, R.color.lime_8))
                textSize = 12f
                gravity = Gravity.END
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f)
            }

            rowLayout.addView(label)
            rowLayout.addView(barWrapper)
            rowLayout.addView(value)
            container.addView(rowLayout)
        }
    }

    private fun buildBreakdownIntro(): String {
        val topCategory = data.breakdownFilters.firstOrNull()?.rows
            ?.filter { it.value > 0.0 }
            ?.maxByOrNull { it.value }

        if (topCategory == null) {
            return "Your carbon footprint breakdown will update here as soon as you complete onboarding, tips, or learning progress."
        }

        val categoryFilter = data.breakdownFilters.firstOrNull {
            it.label.equals("Showing ${topCategory.label.lowercase()}", ignoreCase = true)
        }
        val topSubcategory = categoryFilter?.rows
            ?.filter { it.value > 0.0 }
            ?.maxByOrNull { it.value }

        val categoryKg = String.format("%.1f", topCategory.value / 1000.0)
        return if (topSubcategory != null && topCategory.value > 0.0) {
            val share = ((topSubcategory.value / topCategory.value) * 100).toInt()
            "The largest part of your carbon footprint is ${topCategory.label.lowercase()} at $categoryKg tonnes of CO2 per year. The largest part of this is ${topSubcategory.label.lowercase()} at $share% so this could be a good place to look at reductions."
        } else {
            "The largest part of your carbon footprint is ${topCategory.label.lowercase()} at $categoryKg tonnes of CO2 per year. This is currently your best place to focus for reductions."
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}

data class ProgressData(
    val relativeEmissions: RelativeEmissions,
    val relativeEmissionsParts: RelativeEmissionParts,
    val comparisonVsCitizen: String,
    val comparisonVsGlobal: String,
    val breakdownFilters: List<BreakdownFilter>,
    val personalTipsCount: Int,
    val personalTips: List<MetricRow>,
    val learningCount: Int,
    val learning: List<MetricRow>
)

data class BreakdownFilter(
    val label: String,
    val rows: List<MetricRow>
)

data class RelativeEmissions(
    val you: Double,
    val countryCitizen: Double,
    val global: Double
)

data class RelativeEmissionParts(
    val you: List<Double>,
    val countryCitizen: List<Double>,
    val global: List<Double>
)

data class MetricRow(
    val label: String,
    val value: Double,
    val total: Double = value
)

object MyProgressRepository {
    // Integration point: replace with server endpoint response.
    fun getProgressData(context: Context, answers: OnboardingAnswers?): ProgressData {
        val snapshot = CarbonTrackerCalculator.calculate(context, answers)
        val tipsStats = PersonalTipsStore.getStats(context)
        val latestCheckIn = CarbonTrackerStore.getLatestCheckIn(context)
        val categoriesById = snapshot.categories.associateBy { it.id }
        val transport = categoriesById["transport"]?.kgPerYear ?: 0.0
        val food = categoriesById["food"]?.kgPerYear ?: 0.0
        val home = categoriesById["home"]?.kgPerYear ?: 0.0
        val purchases = categoriesById["purchases"]?.kgPerYear ?: 0.0
        val services = categoriesById["services"]?.kgPerYear ?: 0.0
        val youSegments = buildUserSegments(
            transport = transport,
            food = food,
            home = home,
            purchases = purchases,
            services = services,
            natureOffset = snapshot.natureOffsetKgPerYear,
            flightsPerYear = latestCheckIn?.flightsPerYear ?: 0
        )
        val countrySegments = buildCountryBenchmarkSegments(
            country = answers?.country,
            totalKg = snapshot.countryAverageKgPerYear
        )
        val globalSegments = buildGlobalBenchmarkSegments(snapshot.globalAverageKgPerYear)
        val estimatedAirTravelKg = youSegments.airTravel
        val estimatedLandTravelKg = youSegments.landTravel
        val homeEnergyKg = youSegments.energy
        val directHomeKg = youSegments.home
        val youTons = snapshot.totalKgPerYear / 1000.0
        val countryTons = snapshot.countryAverageKgPerYear / 1000.0
        val globalTons = snapshot.globalAverageKgPerYear / 1000.0

        val allRows = listOf(
            MetricRow("Transport", transport),
            MetricRow("Food", food),
            MetricRow("Home", home),
            MetricRow("Purchases", purchases),
            MetricRow("Services", services),
            MetricRow("Trees", snapshot.natureOffsetKgPerYear)
        )

        return ProgressData(
            relativeEmissions = RelativeEmissions(youTons, countryTons, globalTons),
            relativeEmissionsParts = RelativeEmissionParts(
                you = youSegments.toTonsList(),
                countryCitizen = countrySegments.toTonsList(),
                global = globalSegments.toTonsList()
            ),
            comparisonVsCitizen = snapshot.comparisonVsCountry.replace("%", " %"),
            comparisonVsGlobal = snapshot.comparisonVsGlobal.replace("%", " %"),
            breakdownFilters = listOf(
                BreakdownFilter(
                    label = "Showing all sections",
                    rows = allRows
                ),
                BreakdownFilter(
                    label = "Showing transport",
                    rows = listOf(
                        MetricRow("Car", estimatedLandTravelKg * 0.68),
                        MetricRow("Bus", estimatedLandTravelKg * 0.08),
                        MetricRow("Rail", estimatedLandTravelKg * 0.07),
                        MetricRow("Ferry", estimatedLandTravelKg * 0.03),
                        MetricRow("Tube and Light rail", estimatedLandTravelKg * 0.07),
                        MetricRow("Taxi", estimatedLandTravelKg * 0.07)
                    )
                ),
                BreakdownFilter(
                    label = "Showing air travel",
                    rows = listOf(
                        MetricRow("Short-haul flights", estimatedAirTravelKg * 0.32),
                        MetricRow("Medium-haul flights", estimatedAirTravelKg * 0.38),
                        MetricRow("Long-haul flights", estimatedAirTravelKg * 0.30)
                    )
                ),
                BreakdownFilter(
                    label = "Showing food",
                    rows = listOf(
                        MetricRow("Diet", food * 0.72),
                        MetricRow("Food waste", food * 0.20),
                        MetricRow("Pet food", food * 0.08)
                    )
                ),
                BreakdownFilter(
                    label = "Showing home",
                    rows = listOf(
                        MetricRow("Heating and cooling", directHomeKg * 0.42),
                        MetricRow("Waste", directHomeKg * 0.18),
                        MetricRow("Home improvement", directHomeKg * 0.24),
                        MetricRow("Water", directHomeKg * 0.16)
                    )
                ),
                BreakdownFilter(
                    label = "Showing energy",
                    rows = listOf(
                        MetricRow("Electricity", homeEnergyKg * 0.52),
                        MetricRow("Gas", homeEnergyKg * 0.28),
                        MetricRow("Oil", homeEnergyKg * 0.12),
                        MetricRow("Other fuels", homeEnergyKg * 0.08)
                    )
                ),
                BreakdownFilter(
                    label = "Showing purchases",
                    rows = listOf(
                        MetricRow("Clothing", purchases * 0.24),
                        MetricRow("Electricals", purchases * 0.18),
                        MetricRow("Personal care", purchases * 0.14),
                        MetricRow("Appliances", purchases * 0.13),
                        MetricRow("Furniture", purchases * 0.21),
                        MetricRow("Cleaning", purchases * 0.10)
                    )
                ),
                BreakdownFilter(
                    label = "Showing services",
                    rows = listOf(
                        MetricRow("Financial services", services * 0.18),
                        MetricRow("Accommodation services", services * 0.22),
                        MetricRow("Mobile and internet", services * 0.20),
                        MetricRow("Pharmacy", services * 0.16),
                        MetricRow("Recreation services", services * 0.24)
                    )
                ),
                BreakdownFilter(
                    label = "Showing trees",
                    rows = listOf(
                        MetricRow("Forest protection", snapshot.natureOffsetKgPerYear * 0.55),
                        MetricRow("Tree planting", snapshot.natureOffsetKgPerYear * 0.45)
                    )
                )
            ),
            personalTipsCount = tipsStats.totalCompletionEvents,
            personalTips = listOf(
                MetricRow("Transport", PersonalTipsStore.completionCountForTip(context, "transport_tip").toDouble(), tipsStats.activeDaysCount.coerceAtLeast(1).toDouble()),
                MetricRow("Home energy", PersonalTipsStore.completionCountForTip(context, "energy_tip").toDouble(), tipsStats.activeDaysCount.coerceAtLeast(1).toDouble()),
                MetricRow("Food", PersonalTipsStore.completionCountForTip(context, "food_tip").toDouble(), tipsStats.activeDaysCount.coerceAtLeast(1).toDouble()),
                MetricRow("Shopping", PersonalTipsStore.completionCountForTip(context, "shopping_tip").toDouble(), tipsStats.activeDaysCount.coerceAtLeast(1).toDouble()),
                MetricRow("Home water", PersonalTipsStore.completionCountForTip(context, "water_tip").toDouble(), tipsStats.activeDaysCount.coerceAtLeast(1).toDouble()),
                MetricRow("Trees", PersonalTipsStore.completionCountForTip(context, "trees_tip").toDouble(), tipsStats.activeDaysCount.coerceAtLeast(1).toDouble())
            ),
            learningCount = LearningProgressStore.getTotalCompleted(context),
            learning = listOf(
                MetricRow("Transport", LearningProgressStore.getCategoryCompleted(context, "transport").toDouble(), LearningRepository.getCategory("transport")?.lessons?.size?.toDouble() ?: 1.0),
                MetricRow("Food", LearningProgressStore.getCategoryCompleted(context, "food").toDouble(), LearningRepository.getCategory("food")?.lessons?.size?.toDouble() ?: 1.0),
                MetricRow("Home", LearningProgressStore.getCategoryCompleted(context, "home").toDouble(), LearningRepository.getCategory("home")?.lessons?.size?.toDouble() ?: 1.0),
                MetricRow("Purchases", LearningProgressStore.getCategoryCompleted(context, "purchases").toDouble(), LearningRepository.getCategory("purchases")?.lessons?.size?.toDouble() ?: 1.0),
                MetricRow("Trees", LearningProgressStore.getCategoryCompleted(context, "trees").toDouble(), LearningRepository.getCategory("trees")?.lessons?.size?.toDouble() ?: 1.0)
            )
        )
    }

    private fun buildUserSegments(
        transport: Double,
        food: Double,
        home: Double,
        purchases: Double,
        services: Double,
        natureOffset: Double,
        flightsPerYear: Int
    ): EmissionSegments {
        val estimatedAirTravel = if (flightsPerYear > 0) {
            (flightsPerYear * 180.0).coerceAtMost(transport * 0.7)
        } else {
            0.0
        }
        val landTravel = (transport - estimatedAirTravel).coerceAtLeast(0.0)

        val energy = (home * 0.62)
        val directHome = (home * 0.38)
        val purchasesWithServices = purchases + services

        val positiveSegments = EmissionSegments(
            landTravel = landTravel,
            airTravel = estimatedAirTravel,
            food = food,
            home = directHome,
            energy = energy,
            purchases = purchasesWithServices
        )
        return positiveSegments.applyOffset(natureOffset)
    }

    private fun buildCountryBenchmarkSegments(country: String?, totalKg: Double): EmissionSegments {
        val shares = when (country?.trim()) {
            "United States" -> doubleArrayOf(0.23, 0.11, 0.14, 0.14, 0.14, 0.24)
            "Canada" -> doubleArrayOf(0.24, 0.08, 0.15, 0.17, 0.15, 0.21)
            "Germany" -> doubleArrayOf(0.18, 0.07, 0.18, 0.18, 0.17, 0.22)
            "United Kingdom" -> doubleArrayOf(0.17, 0.09, 0.18, 0.17, 0.15, 0.24)
            "France" -> doubleArrayOf(0.17, 0.06, 0.20, 0.17, 0.13, 0.27)
            "Italy" -> doubleArrayOf(0.19, 0.07, 0.20, 0.16, 0.12, 0.26)
            "Spain" -> doubleArrayOf(0.18, 0.08, 0.21, 0.15, 0.12, 0.26)
            "Poland" -> doubleArrayOf(0.19, 0.05, 0.18, 0.20, 0.17, 0.21)
            "Hungary" -> doubleArrayOf(0.19, 0.05, 0.19, 0.20, 0.15, 0.22)
            "Ukraine" -> doubleArrayOf(0.16, 0.03, 0.24, 0.22, 0.14, 0.21)
            "India" -> doubleArrayOf(0.15, 0.03, 0.27, 0.21, 0.12, 0.22)
            else ->
                doubleArrayOf(0.21, 0.06, 0.19, 0.19, 0.14, 0.21)
        }
        return EmissionSegments.fromShares(totalKg, shares)
    }

    private fun buildGlobalBenchmarkSegments(totalKg: Double): EmissionSegments {
        return EmissionSegments.fromShares(
            totalKg,
            doubleArrayOf(0.18, 0.05, 0.24, 0.18, 0.12, 0.23)
        )
    }
}

private data class EmissionSegments(
    val landTravel: Double,
    val airTravel: Double,
    val food: Double,
    val home: Double,
    val energy: Double,
    val purchases: Double
) {
    fun toTonsList(): List<Double> = listOf(
        landTravel / 1000.0,
        airTravel / 1000.0,
        food / 1000.0,
        home / 1000.0,
        energy / 1000.0,
        purchases / 1000.0
    )

    fun totalKg(): Double = landTravel + airTravel + food + home + energy + purchases

    fun applyOffset(offsetKg: Double): EmissionSegments {
        val total = totalKg()
        if (offsetKg <= 0.0 || total <= 0.0) return this
        val remainingRatio = ((total - offsetKg).coerceAtLeast(0.0) / total).coerceIn(0.0, 1.0)
        return EmissionSegments(
            landTravel = landTravel * remainingRatio,
            airTravel = airTravel * remainingRatio,
            food = food * remainingRatio,
            home = home * remainingRatio,
            energy = energy * remainingRatio,
            purchases = purchases * remainingRatio
        )
    }

    companion object {
        fun fromShares(totalKg: Double, shares: DoubleArray): EmissionSegments {
            val normalized = if (shares.sum() == 0.0) doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 0.0) else shares
            return EmissionSegments(
                landTravel = totalKg * normalized[0],
                airTravel = totalKg * normalized[1],
                food = totalKg * normalized[2],
                home = totalKg * normalized[3],
                energy = totalKg * normalized[4],
                purchases = totalKg * normalized[5]
            )
        }
    }
}
