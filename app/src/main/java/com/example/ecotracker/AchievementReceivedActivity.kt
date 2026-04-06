package com.example.ecotracker

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class AchievementReceivedActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ACHIEVEMENT_ID = "extra_achievement_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement_received)

        val achievementId = intent.getStringExtra(EXTRA_ACHIEVEMENT_ID).orEmpty()
        val item = AchievementsRepository.getItems().firstOrNull { it.id == achievementId } ?: return

        findViewById<TextView>(R.id.titleText).text = item.title
        findViewById<TextView>(R.id.descriptionText).text = item.description
        findViewById<ImageView>(R.id.achievementImage).loadAsset(item.imageAsset)

        findViewById<MaterialButton>(R.id.claimButton).setOnClickListener {
            AchievementStore.markClaimed(this, item.id)
            goToAchievements()
        }
        findViewById<MaterialButton>(R.id.myAchievementsButton).setOnClickListener { goToAchievements() }
    }

    private fun goToAchievements() {
        startActivity(Intent(this, AchievementsActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        })
        finish()
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
