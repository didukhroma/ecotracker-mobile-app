package com.example.ecotracker

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AchievementDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ACHIEVEMENT_ID = "extra_achievement_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement_detail)

        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }

        val achievementId = intent.getStringExtra(EXTRA_ACHIEVEMENT_ID).orEmpty()
        val item = AchievementsRepository.getItems().firstOrNull { it.id == achievementId } ?: return
        val state = AchievementsRepository.getState(this, item)

        findViewById<TextView>(R.id.titleText).text = item.title
        findViewById<TextView>(R.id.descriptionText).text = item.description
        findViewById<ImageView>(R.id.achievementImage).loadAsset(item.imageAsset)

        renderChecks(findViewById(R.id.requirementsContainer), item.requirements, state.unlocked)
        renderChecks(findViewById(R.id.rewardsContainer), item.rewards, state.claimed)
    }

    private fun renderChecks(container: LinearLayout, lines: List<String>, checked: Boolean) {
        container.removeAllViews()
        lines.forEach { text ->
            val check = CheckBox(this).apply {
                isChecked = checked
                isEnabled = false
                this.text = "- $text"
                setTextColor(getColor(R.color.lime_10))
                textSize = 13f
            }
            container.addView(check)
        }
    }

    private fun ImageView.loadAsset(assetName: String) {
        val bitmap = try {
            assets.open(assetName).use { input -> BitmapFactory.decodeStream(input) }
        } catch (_: Exception) {
            val pngFallback = if (assetName.endsWith(".jpg")) assetName.replace(".jpg", ".png") else assetName
            try {
                assets.open(pngFallback).use { input -> BitmapFactory.decodeStream(input) }
            } catch (_: Exception) {
                assets.open("screen_2.png").use { input -> BitmapFactory.decodeStream(input) }
            }
        }
        setImageBitmap(bitmap)
    }
}
