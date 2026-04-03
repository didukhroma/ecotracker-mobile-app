package com.example.ecotracker

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.OnBackPressedCallback

class MainActivity : AppCompatActivity() {
    private lateinit var heroImage: ImageView
    private lateinit var subtitleText: TextView
    private lateinit var bodyText: TextView
    private lateinit var primaryButton: Button
    private lateinit var helperText: TextView
    private lateinit var secondaryButton: Button
    private var currentScreen = OnboardingScreen.SCREEN_2
    private val forceHomeMock: Boolean by lazy {
        intent?.getBooleanExtra("force_home_mock", true) ?: true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (forceHomeMock) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }
        setContentView(R.layout.activity_main)

        heroImage = findViewById(R.id.loadingHeroImage)
        subtitleText = findViewById(R.id.subtitleText)
        bodyText = findViewById(R.id.bodyText)
        primaryButton = findViewById(R.id.primaryButton)
        helperText = findViewById(R.id.helperText)
        secondaryButton = findViewById(R.id.secondaryButton)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentScreen == OnboardingScreen.SCREEN_3) {
                    renderScreen(OnboardingScreen.SCREEN_2)
                } else {
                    finish()
                }
            }
        })

        renderScreen(OnboardingScreen.SCREEN_2)
    }

    override fun onStart() {
        super.onStart()
        if (!forceHomeMock && FirebaseSync.hasAuthenticatedUser(this)) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun renderScreen(screen: OnboardingScreen) {
        currentScreen = screen
        when (screen) {
            OnboardingScreen.SCREEN_2 -> {
                loadAssetImage("screen_2.png")
                subtitleText.setText(R.string.screen_2_subtitle)
                bodyText.setText(R.string.screen_2_body)
                bodyText.visibility = View.VISIBLE
                primaryButton.setText(R.string.next)
                primaryButton.setOnClickListener { renderScreen(OnboardingScreen.SCREEN_3) }
                helperText.visibility = View.GONE
                secondaryButton.visibility = View.GONE
            }
            OnboardingScreen.SCREEN_3 -> {
                loadAssetImage("screen_3.png")
                subtitleText.setText(R.string.screen_3_subtitle)
                bodyText.visibility = View.GONE
                primaryButton.setText(R.string.sign_up_free)
                primaryButton.setOnClickListener {
                    startActivity(Intent(this, SignUpActivity::class.java))
                }
                helperText.visibility = View.VISIBLE
                secondaryButton.visibility = View.VISIBLE
                secondaryButton.setOnClickListener {
                    startActivity(Intent(this, SignInActivity::class.java))
                }
            }
        }
    }

    private fun loadAssetImage(assetName: String) {
        assets.open(assetName).use { input ->
            heroImage.setImageBitmap(BitmapFactory.decodeStream(input))
        }
    }

    private enum class OnboardingScreen {
        SCREEN_2,
        SCREEN_3
    }
}
