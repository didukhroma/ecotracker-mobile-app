package com.example.ecotracker

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Patterns
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignInActivity : AppCompatActivity() {
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var signInButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        findViewById<ImageView>(R.id.signInHeroImage).loadAsset("screen_4.png")

        emailLayout = findViewById(R.id.emailLayout)
        passwordLayout = findViewById(R.id.passwordLayout)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        signInButton = findViewById(R.id.signInButton)
        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }

        emailInput.doAfterTextChanged {
            if (emailLayout.error != null) validateEmail(showErrors = false)
            updateSignInButtonState()
        }
        passwordInput.doAfterTextChanged {
            if (passwordLayout.error != null) validatePassword(showErrors = false)
            updateSignInButtonState()
        }

        findViewById<TextView>(R.id.forgotPasswordLink).setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        findViewById<MaterialButton>(R.id.googleButton).setOnClickListener {
            showMockMessage(getString(R.string.google_sign_in_mock))
        }
        findViewById<MaterialButton>(R.id.facebookButton).setOnClickListener {
            showMockMessage(getString(R.string.facebook_sign_in_mock))
        }
        signInButton.setOnClickListener {
            val emailValid = validateEmail(showErrors = true)
            val passwordValid = validatePassword(showErrors = true)
            if (emailValid && passwordValid) {
                startActivity(Intent(this, GreetingActivity::class.java))
            }
        }

        updateSignInButtonState()
    }

    private fun validateEmail(showErrors: Boolean): Boolean {
        val email = emailInput.text?.toString()?.trim().orEmpty()
        val valid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        emailLayout.error = if (!valid && showErrors) getString(R.string.error_invalid_email) else null
        return valid
    }

    private fun validatePassword(showErrors: Boolean): Boolean {
        val password = passwordInput.text?.toString().orEmpty()
        val valid = password.length >= 8
        passwordLayout.error = if (!valid && showErrors) getString(R.string.error_invalid_password) else null
        return valid
    }

    private fun updateSignInButtonState() {
        val enabled = Patterns.EMAIL_ADDRESS.matcher(emailInput.text?.toString()?.trim().orEmpty()).matches() &&
            passwordInput.text?.toString().orEmpty().length >= 8
        signInButton.isEnabled = enabled
        signInButton.alpha = if (enabled) 1f else 0.45f
    }

    private fun ImageView.loadAsset(assetName: String) {
        assets.open(assetName).use { input ->
            setImageBitmap(BitmapFactory.decodeStream(input))
        }
    }

    private fun showMockMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
