package com.example.ecotracker

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
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
        findViewById<ImageView>(R.id.closeSidebar).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        findViewById<TextView>(R.id.sidebarCarbonTracker).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, CarbonEmissionTrackerActivity::class.java))
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
        findViewById<TextView>(R.id.learnMoreText).setOnClickListener {
            startActivity(Intent(this, PersonalTipsActivity::class.java))
        }
        findViewById<TextView>(R.id.sidebarCommunity).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, CommunityProfileActivity::class.java))
        }
        findViewById<TextView>(R.id.sidebarAchievements).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, AchievementsActivity::class.java))
        }
        findViewById<TextView>(R.id.sidebarSettings).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        findViewById<android.view.View>(R.id.logoutText).setOnClickListener {
            FirebaseSync.signOut()
            HomeCacheStore.invalidate(this)
            OnboardingSessionStore.latestPayload = null
            finishAffinity()
        }

        findViewById<ImageView>(R.id.homeImage).loadAsset("home.png")

        val onboardingPayload = intent.getStringExtra(QuestionActivity.EXTRA_ONBOARDING_PAYLOAD)
            ?: OnboardingSessionStore.latestPayload

        loadHomeData(onboardingPayload)

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

    private fun loadHomeData(onboardingPayload: String?) {
        FirebaseSync.fetchHomeData(this) { remoteData ->
            val fallbackData = HomeRepository.defaultHomeData(
                firstName = HomeRepository.parseFirstName(onboardingPayload).orEmpty(),
                onboardingCompleted = false
            )
            val resolvedData = if (remoteData.userName.isBlank()) fallbackData else remoteData
            bindHomeData(resolvedData)
        }
    }

    private fun bindHomeData(data: HomeData) {
        val displayName = normalizeName(data.userName)
        val greetingName = displayName.substringBefore(" ").ifBlank { "John" }
        findViewById<TextView>(R.id.greetingText).text = getString(R.string.home_greeting_format, greetingName)
        findViewById<TextView>(R.id.tipsText).text = data.tips.joinToString("\n") { "• $it" }
        findViewById<TextView>(R.id.footprintValueText).text = data.footprint
        findViewById<TextView>(R.id.challengesValueText).text = data.challengesParticipated
        findViewById<TextView>(R.id.treesValueText).text = data.treesSaved
        findViewById<TextView>(R.id.emissionReducedValueText).text = data.emissionReduced

        findViewById<TextView>(R.id.sidebarName).text = displayName
        findViewById<TextView>(R.id.sidebarQuote).text = data.quote
        bindAvatar(displayName, data.photoBase64)

        val onboardingBanner = findViewById<TextView>(R.id.completeOnboardingButton)
        onboardingBanner.isVisible = !data.onboardingCompleted
        onboardingBanner.setOnClickListener {
            startActivity(Intent(this, QuestionActivity::class.java))
        }
    }

    private fun bindAvatar(name: String, photoBase64: String?) {
        val avatarImage = findViewById<ImageView>(R.id.sidebarAvatarImage)
        val avatarInitial = findViewById<TextView>(R.id.sidebarAvatarInitial)
        val initial = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
        avatarInitial.text = initial

        val bitmap = decodeBase64Bitmap(photoBase64)
        if (bitmap == null) {
            avatarImage.visibility = View.GONE
            avatarInitial.visibility = View.VISIBLE
            return
        }
        avatarImage.setImageBitmap(bitmap)
        avatarImage.visibility = View.VISIBLE
        avatarInitial.visibility = View.GONE
    }

    private fun decodeBase64Bitmap(photoBase64: String?): Bitmap? {
        if (photoBase64.isNullOrBlank()) return null
        return try {
            val bytes = Base64.decode(photoBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (_: Exception) {
            null
        }
    }

    private fun normalizeName(name: String): String {
        val trimmed = name.trim()
        if (trimmed.isBlank()) return "John"
        return trimmed.split(" ")
            .filter { it.isNotBlank() }
            .joinToString(" ") { token ->
                token.lowercase().replaceFirstChar { ch -> ch.titlecase() }
            }
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
    val quote: String,
    val onboardingCompleted: Boolean,
    val photoBase64: String? = null,
    val cachedAtMs: Long = 0L
)

object HomeRepository {
    // Integration point: replace with real server request when backend endpoint is ready.
    fun defaultHomeData(
        firstName: String? = null,
        onboardingCompleted: Boolean = false,
        photoBase64: String? = null
    ): HomeData {
        val displayName = if (firstName.isNullOrBlank()) "John" else firstName

        return HomeData(
            userName = displayName,
            tips = listOf(
                "Buy energy-saving bulbs",
                "Use eco mode on dishwasher",
                "Eat more vegetables",
                "Buy less clothes"
            ),
            footprint = "0 kg",
            challengesParticipated = "0",
            treesSaved = "0",
            emissionReduced = "0 kg",
            quote = "Small steps, big impact - reduce your footprint!",
            onboardingCompleted = onboardingCompleted,
            photoBase64 = photoBase64
        )
    }

    fun parseFirstName(payload: String?): String? {
        return try {
            if (payload.isNullOrBlank()) null else JSONObject(payload).optString("firstName").takeIf { it.isNotBlank() }
        } catch (_: Exception) {
            null
        }
    }
}
