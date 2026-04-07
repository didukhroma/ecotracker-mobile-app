package com.example.ecotracker

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class CarbonEmissionTrackerActivity : AppCompatActivity() {

    private lateinit var totalValue: TextView
    private lateinit var confidenceValue: TextView
    private lateinit var countryComparisonValue: TextView
    private lateinit var countryAverageLabel: TextView
    private lateinit var globalComparisonValue: TextView
    private lateinit var globalAverageLabel: TextView
    private lateinit var natureOffsetValue: TextView
    private lateinit var categoryContainer: LinearLayout
    private lateinit var recommendationsContainer: LinearLayout
    private lateinit var carKmInput: TextInputEditText
    private lateinit var flightsInput: TextInputEditText
    private lateinit var homeEnergyInput: TextInputEditText
    private lateinit var shoppingInput: TextInputEditText
    private lateinit var saveCheckInButton: MaterialButton
    private var latestAnswers: OnboardingAnswers? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carbon_emission_tracker)

        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }
        totalValue = findViewById(R.id.totalValue)
        confidenceValue = findViewById(R.id.confidenceValue)
        countryComparisonValue = findViewById(R.id.countryComparisonValue)
        countryAverageLabel = findViewById(R.id.countryAverageLabel)
        globalComparisonValue = findViewById(R.id.globalComparisonValue)
        globalAverageLabel = findViewById(R.id.globalAverageLabel)
        natureOffsetValue = findViewById(R.id.natureOffsetValue)
        categoryContainer = findViewById(R.id.categoryContainer)
        recommendationsContainer = findViewById(R.id.recommendationsContainer)
        carKmInput = findViewById(R.id.carKmInput)
        flightsInput = findViewById(R.id.flightsInput)
        homeEnergyInput = findViewById(R.id.homeEnergyInput)
        shoppingInput = findViewById(R.id.shoppingInput)
        saveCheckInButton = findViewById(R.id.saveCheckInButton)

        prefillLatestCheckIn()
        saveCheckInButton.setOnClickListener { saveCheckIn() }

        FirebaseSync.fetchOnboardingAnswers(this) { answers ->
            latestAnswers = answers
            val snapshot = CarbonTrackerCalculator.calculate(this, answers)
            bindSnapshot(snapshot)
            FirebaseSync.saveCarbonTrackerSnapshot(this, snapshot)
        }
    }

    private fun bindSnapshot(snapshot: CarbonTrackerSnapshot) {
        totalValue.text = formatKg(snapshot.totalKgPerYear)
        confidenceValue.text = snapshot.confidenceLabel
        countryComparisonValue.text = snapshot.comparisonVsCountry
        countryAverageLabel.text = getString(
            R.string.carbon_tracker_country_average_format,
            formatKg(snapshot.countryAverageKgPerYear)
        )
        globalComparisonValue.text = snapshot.comparisonVsGlobal
        globalAverageLabel.text = getString(
            R.string.carbon_tracker_global_average_format,
            formatKg(snapshot.globalAverageKgPerYear)
        )
        val natureOffsetDisplay = if (kotlin.math.abs(snapshot.natureOffsetKgPerYear) < 0.5) {
            formatKg(0.0)
        } else {
            "-${formatKg(snapshot.natureOffsetKgPerYear)}"
        }
        natureOffsetValue.text = natureOffsetDisplay

        renderCategories(snapshot.categories)
        renderRecommendations(snapshot.recommendations)
    }

    private fun renderCategories(categories: List<CarbonCategoryScore>) {
        categoryContainer.removeAllViews()
        val maxValue = categories.maxOfOrNull { it.kgPerYear }?.coerceAtLeast(1.0) ?: 1.0

        categories.forEach { category ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).also { it.topMargin = dp(10) }
            }

            val topLine = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
            }

            val dot = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(dp(14), dp(14)).also { it.marginEnd = dp(8) }
                setBackgroundColor(ContextCompat.getColor(this@CarbonEmissionTrackerActivity, category.colorRes))
            }

            val title = TextView(this).apply {
                text = category.title
                setTextColor(ContextCompat.getColor(this@CarbonEmissionTrackerActivity, R.color.lime_10))
                textSize = 15f
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val value = TextView(this).apply {
                text = formatKg(category.kgPerYear)
                setTextColor(ContextCompat.getColor(this@CarbonEmissionTrackerActivity, R.color.lime_8))
                textSize = 14f
            }

            topLine.addView(dot)
            topLine.addView(title)
            topLine.addView(value)

            val barBackground = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp(10)
                ).also { it.topMargin = dp(6) }
                setBackgroundColor(ContextCompat.getColor(this@CarbonEmissionTrackerActivity, R.color.lime_2))
            }

            val ratio = (category.kgPerYear / maxValue).toFloat().coerceIn(0.05f, 1f)
            val barFill = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, ratio)
                setBackgroundColor(ContextCompat.getColor(this@CarbonEmissionTrackerActivity, category.colorRes))
            }
            val barRest = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f - ratio)
            }
            barBackground.addView(barFill)
            barBackground.addView(barRest)

            row.addView(topLine)
            row.addView(barBackground)
            categoryContainer.addView(row)
        }
    }

    private fun renderRecommendations(recommendations: List<CarbonRecommendation>) {
        recommendationsContainer.removeAllViews()
        recommendations.forEach { recommendation ->
            val card = MaterialCardView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).also { it.topMargin = dp(10) }
                radius = dp(14).toFloat()
                strokeColor = ContextCompat.getColor(this@CarbonEmissionTrackerActivity, R.color.lime_6)
                strokeWidth = dp(1)
                cardElevation = 0f
                setCardBackgroundColor(ContextCompat.getColor(this@CarbonEmissionTrackerActivity, R.color.lime_2))
            }

            val content = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(14), dp(14), dp(14), dp(14))
            }

            val title = TextView(this).apply {
                text = recommendation.title
                setTextColor(ContextCompat.getColor(this@CarbonEmissionTrackerActivity, R.color.lime_10))
                textSize = 15f
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            }

            val description = TextView(this).apply {
                text = recommendation.description
                setTextColor(ContextCompat.getColor(this@CarbonEmissionTrackerActivity, R.color.lime_8))
                textSize = 13f
                setLineSpacing(0f, 1.1f)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).also { it.topMargin = dp(4) }
            }

            content.addView(title)
            content.addView(description)
            card.addView(content)
            recommendationsContainer.addView(card)
        }
    }

    private fun prefillLatestCheckIn() {
        val latest = CarbonTrackerStore.getLatestCheckIn(this) ?: return
        carKmInput.setText(latest.carKmPerMonth.toString())
        flightsInput.setText(latest.flightsPerYear.toString())
        homeEnergyInput.setText(latest.homeEnergyDeltaPercent.toString())
        shoppingInput.setText(latest.shoppingDeltaPercent.toString())
    }

    private fun saveCheckIn() {
        val checkIn = CarbonCheckIn(
            monthKey = CarbonTrackerStore.currentMonthKey(),
            carKmPerMonth = carKmInput.text?.toString()?.toIntOrNull() ?: 0,
            flightsPerYear = flightsInput.text?.toString()?.toIntOrNull() ?: 0,
            homeEnergyDeltaPercent = homeEnergyInput.text?.toString()?.toIntOrNull() ?: 0,
            shoppingDeltaPercent = shoppingInput.text?.toString()?.toIntOrNull() ?: 0,
            savedAtMs = System.currentTimeMillis()
        )
        CarbonTrackerStore.setLatestCheckIn(this, checkIn)
        val refreshed = CarbonTrackerCalculator.calculate(this, latestAnswers)
        CarbonTrackerStore.saveCheckIn(this, checkIn, refreshed)
        bindSnapshot(refreshed)
        FirebaseSync.saveCarbonTrackerSnapshot(this, refreshed)
        saveCheckInButton.text = getString(R.string.carbon_tracker_saved)
        saveCheckInButton.postDelayed({
            saveCheckInButton.text = getString(R.string.carbon_tracker_save_checkin)
        }, 1200)
    }

    private fun formatKg(value: Double): String {
        val normalized = if (kotlin.math.abs(value) < 0.5) 0.0 else value
        return if (normalized >= 1000) {
            String.format("%.1f t CO2/year", normalized / 1000.0)
        } else {
            String.format("%.0f kg CO2/year", normalized)
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
