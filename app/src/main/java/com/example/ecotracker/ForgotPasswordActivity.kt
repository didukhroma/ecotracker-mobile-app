package com.example.ecotracker

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

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var emailLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var sendEmailButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        findViewById<ImageView>(R.id.forgotHeroImage).loadAsset("forgot.png")
        emailLayout = findViewById(R.id.emailLayout)
        emailInput = findViewById(R.id.emailInput)
        sendEmailButton = findViewById(R.id.sendEmailButton)

        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }
        emailInput.doAfterTextChanged {
            if (emailLayout.error != null) validateEmail(showErrors = false)
            updateSendButtonState()
        }
        sendEmailButton.setOnClickListener {
            if (validateEmail(showErrors = true)) {
                sendResetViaFirebaseOrFallback()
            }
        }

        updateSendButtonState()
    }

    private fun validateEmail(showErrors: Boolean): Boolean {
        val email = emailInput.text?.toString()?.trim().orEmpty()
        val valid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        emailLayout.error = if (!valid && showErrors) getString(R.string.error_invalid_email) else null
        return valid
    }

    private fun updateSendButtonState() {
        val enabled = Patterns.EMAIL_ADDRESS.matcher(emailInput.text?.toString()?.trim().orEmpty()).matches()
        sendEmailButton.isEnabled = enabled
        sendEmailButton.alpha = if (enabled) 1f else 0.4f
    }

    private fun sendResetViaFirebaseOrFallback() {
        val email = emailInput.text?.toString()?.trim().orEmpty()

        if (!FirebaseSync.isAvailable(this)) {
            Toast.makeText(this, getString(R.string.firebase_not_configured_fallback), Toast.LENGTH_SHORT).show()
            return
        }

        sendEmailButton.isEnabled = false
        FirebaseSync.sendPasswordReset(
            context = this,
            email = email,
            onSuccess = {
                Toast.makeText(this, getString(R.string.forgot_password_email_sent), Toast.LENGTH_SHORT).show()
                finish()
            },
            onError = { message ->
                sendEmailButton.isEnabled = true
                updateSendButtonState()
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun ImageView.loadAsset(assetName: String) {
        assets.open(assetName).use { input ->
            setImageBitmap(BitmapFactory.decodeStream(input))
        }
    }
}
