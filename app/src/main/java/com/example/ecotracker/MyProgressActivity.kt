package com.example.ecotracker

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.math.max

class MyProgressActivity : AppCompatActivity() {

    private lateinit var data: ProgressData
    private lateinit var breakdownListContainer: LinearLayout
    private lateinit var personalTipsListContainer: LinearLayout
    private lateinit var learningListContainer: LinearLayout
    private lateinit var filterDropdown: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_progress)

        data = MyProgressRepository.getProgressData()
        breakdownListContainer = findViewById(R.id.breakdownListContainer)
        personalTipsListContainer = findViewById(R.id.personalTipsListContainer)
        learningListContainer = findViewById(R.id.learningListContainer)
        filterDropdown = findViewById(R.id.breakdownFilterDropdown)

        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }

        bindTopSection()
        bindBreakdown()
        bindPersonalTips()
        bindLearningWay()
    }

    private fun bindTopSection() {
        findViewById<TextView>(R.id.valueYou).text = data.relativeEmissions.you.toString()
        findViewById<TextView>(R.id.valueCitizen).text = data.relativeEmissions.countryCitizen.toString()
        findViewById<TextView>(R.id.valueGlobal).text = data.relativeEmissions.global.toString()
        findViewById<TextView>(R.id.citizenComparisonValue).text = data.comparisonVsCitizen
        findViewById<TextView>(R.id.globalComparisonValue).text = data.comparisonVsGlobal
        findViewById<TextView>(R.id.breakdownIntro).text = getString(R.string.carbon_breakdown_intro)

        renderStackedBar(findViewById(R.id.barYou), data.relativeEmissionsParts.you)
        renderStackedBar(findViewById(R.id.barCitizen), data.relativeEmissionsParts.countryCitizen)
        renderStackedBar(findViewById(R.id.barGlobal), data.relativeEmissionsParts.global)
    }

    private fun bindBreakdown() {
        val filterItems = listOf(getString(R.string.breakdown_filter_all))
        filterDropdown.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_1, filterItems))
        filterDropdown.setText(filterItems.first(), false)
        filterDropdown.keyListener = null
        filterDropdown.setOnClickListener { filterDropdown.showDropDown() }

        renderMetricRows(
            container = breakdownListContainer,
            rows = data.breakdown,
            unitLabel = getString(R.string.kg_unit),
            maxValue = data.breakdown.maxOfOrNull { it.value } ?: 1.0
        )
    }

    private fun bindPersonalTips() {
        findViewById<TextView>(R.id.personalTipsCount).text = data.personalTipsCount.toString()
        renderMetricRows(
            container = personalTipsListContainer,
            rows = data.personalTips,
            unitLabel = getString(R.string.tips_unit),
            maxValue = max(1.0, data.personalTips.maxOfOrNull { it.value } ?: 1.0)
        )
    }

    private fun bindLearningWay() {
        findViewById<TextView>(R.id.learningCount).text = data.learningCount.toString()
        renderMetricRows(
            container = learningListContainer,
            rows = data.learning,
            unitLabel = getString(R.string.lessons_unit),
            maxValue = max(1.0, data.learning.maxOfOrNull { it.value } ?: 1.0)
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
        unitLabel: String,
        maxValue: Double
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
                    (row.value / maxValue).toFloat().coerceAtLeast(0f)
                )
            }
            val barEmpty = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (1f - (row.value / maxValue).toFloat().coerceIn(0f, 1f))
                )
            }
            barWrapper.addView(barFill)
            barWrapper.addView(barEmpty)

            val value = TextView(this).apply {
                text = if (unitLabel == getString(R.string.kg_unit)) {
                    String.format("%.3f %s", row.value, unitLabel)
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

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}

data class ProgressData(
    val relativeEmissions: RelativeEmissions,
    val relativeEmissionsParts: RelativeEmissionParts,
    val comparisonVsCitizen: String,
    val comparisonVsGlobal: String,
    val breakdown: List<MetricRow>,
    val personalTipsCount: Int,
    val personalTips: List<MetricRow>,
    val learningCount: Int,
    val learning: List<MetricRow>
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
    val value: Double
)

object MyProgressRepository {
    // Integration point: replace with server endpoint response.
    fun getProgressData(): ProgressData {
        return ProgressData(
            relativeEmissions = RelativeEmissions(15.8, 19.6, 5.1),
            relativeEmissionsParts = RelativeEmissionParts(
                you = listOf(3.6, 1.2, 2.1, 4.0, 2.0, 2.9),
                countryCitizen = listOf(4.3, 1.8, 3.8, 5.6, 1.5, 2.6),
                global = listOf(1.2, 0.7, 1.1, 1.0, 0.5, 0.6)
            ),
            comparisonVsCitizen = "19 % less",
            comparisonVsGlobal = "309 % more",
            breakdown = listOf(
                MetricRow("Transport", 5.124),
                MetricRow("Food", 3.524),
                MetricRow("Home", 3.024),
                MetricRow("Purchases", 2.324),
                MetricRow("Services", 1.824),
                MetricRow("Tree", 0.0)
            ),
            personalTipsCount = 1,
            personalTips = listOf(
                MetricRow("Planet saver", 1.0),
                MetricRow("Big impact", 0.0),
                MetricRow("Decent impact", 0.0),
                MetricRow("Good impact", 0.0),
                MetricRow("Small impact", 0.0)
            ),
            learningCount = 0,
            learning = listOf(
                MetricRow("Transport", 0.0),
                MetricRow("Food", 0.0),
                MetricRow("Home", 0.0),
                MetricRow("Purchases", 0.0),
                MetricRow("Planet", 0.0)
            )
        )
    }
}
