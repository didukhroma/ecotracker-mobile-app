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
        val categoriesById = snapshot.categories.associateBy { it.id }
        val transport = categoriesById["transport"]?.kgPerYear ?: 0.0
        val food = categoriesById["food"]?.kgPerYear ?: 0.0
        val home = categoriesById["home"]?.kgPerYear ?: 0.0
        val purchases = categoriesById["purchases"]?.kgPerYear ?: 0.0
        val services = categoriesById["services"]?.kgPerYear ?: 0.0
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
                you = listOf(
                    transport * 0.82,
                    transport * 0.18,
                    food,
                    home * 0.65,
                    (home * 0.35) + (services * 0.45),
                    purchases + (services * 0.55)
                ).map { it / 1000.0 },
                countryCitizen = listOf(
                    countryTons * 0.26,
                    countryTons * 0.08,
                    countryTons * 0.19,
                    countryTons * 0.18,
                    countryTons * 0.12,
                    countryTons * 0.17
                ),
                global = listOf(
                    globalTons * 0.24,
                    globalTons * 0.06,
                    globalTons * 0.22,
                    globalTons * 0.20,
                    globalTons * 0.11,
                    globalTons * 0.17
                )
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
                        MetricRow("Car", transport * 0.68),
                        MetricRow("Bus", transport * 0.08),
                        MetricRow("Rail", transport * 0.07),
                        MetricRow("Ferry", transport * 0.03),
                        MetricRow("Tube and Light rail", transport * 0.07),
                        MetricRow("Taxi", transport * 0.07)
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
                        MetricRow("Gas", home * 0.24),
                        MetricRow("Electricity", home * 0.28),
                        MetricRow("Waste", home * 0.10),
                        MetricRow("Home improvement", home * 0.16),
                        MetricRow("Water", home * 0.12),
                        MetricRow("Oil", home * 0.10)
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
            personalTipsCount = PersonalTipsStore.selectedCount(context),
            personalTips = listOf(
                MetricRow("Transport", if (PersonalTipsStore.isSelected(context, "transport_tip")) 1.0 else 0.0, 1.0),
                MetricRow("Home energy", if (PersonalTipsStore.isSelected(context, "energy_tip")) 1.0 else 0.0, 1.0),
                MetricRow("Food", if (PersonalTipsStore.isSelected(context, "food_tip")) 1.0 else 0.0, 1.0),
                MetricRow("Shopping", if (PersonalTipsStore.isSelected(context, "shopping_tip")) 1.0 else 0.0, 1.0),
                MetricRow("Home water", if (PersonalTipsStore.isSelected(context, "water_tip")) 1.0 else 0.0, 1.0),
                MetricRow("Trees", if (PersonalTipsStore.isSelected(context, "trees_tip")) 1.0 else 0.0, 1.0)
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
}
