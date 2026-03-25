package com.example.ecotracker

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_placeholder)

        drawerLayout = findViewById(R.id.drawerLayout)

        findViewById<ImageButton>(R.id.menuButton).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        findViewById<TextView>(R.id.closeSidebar).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        findViewById<TextView>(R.id.sidebarMyProgress).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, MyProgressActivity::class.java))
        }
        findViewById<TextView>(R.id.sidebarLearningPlatform).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, LearningPlatformActivity::class.java))
        }
        findViewById<TextView>(R.id.sidebarPersonalTips).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, PersonalTipsActivity::class.java))
        }
        findViewById<TextView>(R.id.sidebarCommunity).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, CommunityProfileActivity::class.java))
        }

        findViewById<TextView>(R.id.logoutText).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            finishAffinity()
        }

        findViewById<ImageView>(R.id.homeImage).loadAsset("home.png")

        val onboardingPayload = intent.getStringExtra(QuestionActivity.EXTRA_ONBOARDING_PAYLOAD)
            ?: OnboardingSessionStore.latestPayload

        val homeData = HomeRepository.getHomeData(onboardingPayload)
        bindHomeData(homeData)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    finish()
                }
            }
        })
    }

    private fun bindHomeData(data: HomeData) {
        findViewById<TextView>(R.id.greetingText).text = getString(R.string.home_greeting_format, data.userName)
        findViewById<TextView>(R.id.tipsText).text = data.tips.joinToString("\n") { "• $it" }
        findViewById<TextView>(R.id.footprintValueText).text = data.footprint
        findViewById<TextView>(R.id.challengesValueText).text = data.challengesParticipated
        findViewById<TextView>(R.id.treesValueText).text = data.treesSaved
        findViewById<TextView>(R.id.emissionReducedValueText).text = data.emissionReduced

        findViewById<TextView>(R.id.sidebarName).text = data.userName
        findViewById<TextView>(R.id.sidebarQuote).text = data.quote
    }

    private fun ImageView.loadAsset(assetName: String) {
        assets.open(assetName).use { input ->
            setImageBitmap(BitmapFactory.decodeStream(input))
        }
    }
}

data class HomeData(
    val userName: String,
    val tips: List<String>,
    val footprint: String,
    val challengesParticipated: String,
    val treesSaved: String,
    val emissionReduced: String,
    val quote: String
)

object HomeRepository {
    // Integration point: replace with real server request when backend endpoint is ready.
    fun getHomeData(onboardingPayload: String?): HomeData {
        val firstName = parseFirstName(onboardingPayload)
        val displayName = if (firstName.isNullOrBlank()) "John" else firstName

        return HomeData(
            userName = displayName,
            tips = listOf(
                "Buy energy-saving bulbs",
                "Use eco mode on dishwasher",
                "Eat more vegetables",
                "Buy less clothes"
            ),
            footprint = "4000 kg",
            challengesParticipated = "3",
            treesSaved = "100",
            emissionReduced = "100 kg",
            quote = "Small steps, big impact - reduce your footprint!"
        )
    }

    private fun parseFirstName(payload: String?): String? {
        return try {
            if (payload.isNullOrBlank()) null else JSONObject(payload).optString("firstName").takeIf { it.isNotBlank() }
        } catch (_: Exception) {
            null
        }
    }
}
