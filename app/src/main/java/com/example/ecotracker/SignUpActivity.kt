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

class SignUpActivity : AppCompatActivity() {
    private lateinit var nameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var nameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var signUpButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        findViewById<ImageView>(R.id.signUpHeroImage).loadAsset("screen_5.png")

        nameLayout = findViewById(R.id.nameLayout)
        emailLayout = findViewById(R.id.emailLayout)
        passwordLayout = findViewById(R.id.passwordLayout)
        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        signUpButton = findViewById(R.id.signUpButton)
        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }

        nameInput.doAfterTextChanged {
            if (nameLayout.error != null) validateName(showErrors = false)
            updateSignUpButtonState()
        }
        emailInput.doAfterTextChanged {
            if (emailLayout.error != null) validateEmail(showErrors = false)
            updateSignUpButtonState()
        }
        passwordInput.doAfterTextChanged {
            if (passwordLayout.error != null) validatePassword(showErrors = false)
            updateSignUpButtonState()
        }

        signUpButton.setOnClickListener {
            val nameValid = validateName(showErrors = true)
            val emailValid = validateEmail(showErrors = true)
            val passwordValid = validatePassword(showErrors = true)
            if (nameValid && emailValid && passwordValid) {
                registerWithFirebaseOrFallback()
            }
        }

        updateSignUpButtonState()
    }

    private fun validateName(showErrors: Boolean): Boolean {
        val name = nameInput.text?.toString()?.trim().orEmpty()
        val valid = name.isNotEmpty()
        nameLayout.error = null
        return valid
    }

    private fun validateEmail(showErrors: Boolean): Boolean {
        val email = emailInput.text?.toString()?.trim().orEmpty()
        val valid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        emailLayout.error = null
        return valid
    }

    private fun validatePassword(showErrors: Boolean): Boolean {
        val password = passwordInput.text?.toString().orEmpty()
        val valid = password.length >= 8
        passwordLayout.error = null
        return valid
    }

    private fun updateSignUpButtonState() {
        val enabled = nameInput.text?.toString()?.trim().orEmpty().isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(emailInput.text?.toString()?.trim().orEmpty()).matches() &&
            passwordInput.text?.toString().orEmpty().length >= 8
        signUpButton.isEnabled = enabled
        signUpButton.alpha = if (enabled) 1f else 0.4f
    }

    private fun ImageView.loadAsset(assetName: String) {
        assets.open(assetName).use { input ->
            setImageBitmap(BitmapFactory.decodeStream(input))
        }
    }

    private fun registerWithFirebaseOrFallback() {
        val name = nameInput.text?.toString()?.trim().orEmpty()
        val email = emailInput.text?.toString()?.trim().orEmpty()
        val password = passwordInput.text?.toString().orEmpty()

        if (!FirebaseSync.isAvailable(this)) {
            Toast.makeText(this, getString(R.string.firebase_not_configured_fallback), Toast.LENGTH_SHORT).show()
            openGreeting()
            return
        }

        signUpButton.isEnabled = false
        FirebaseSync.registerUser(
            context = this,
            name = name,
            email = email,
            password = password,
            onSuccess = { openGreeting() },
            onError = { message ->
                signUpButton.isEnabled = true
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                updateSignUpButtonState()
            }
        )
    }

    private fun openGreeting() {
        startActivity(
            Intent(this, GreetingActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        finish()
    }
}
