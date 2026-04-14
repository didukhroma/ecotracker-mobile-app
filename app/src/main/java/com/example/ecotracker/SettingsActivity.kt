package com.example.ecotracker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.io.ByteArrayOutputStream

class SettingsActivity : AppCompatActivity() {

    private lateinit var avatarImage: ImageView
    private lateinit var avatarInitial: TextView
    private lateinit var firstNameInput: TextInputEditText
    private lateinit var lastNameInput: TextInputEditText
    private lateinit var saveButton: MaterialButton

    private var photoBase64: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        avatarImage = findViewById(R.id.avatarImage)
        avatarInitial = findViewById(R.id.avatarInitial)
        firstNameInput = findViewById(R.id.firstNameInput)
        lastNameInput = findViewById(R.id.lastNameInput)
        saveButton = findViewById(R.id.saveButton)

        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }
        findViewById<MaterialButton>(R.id.changePhotoButton).setOnClickListener {
            Toast.makeText(this, getString(R.string.in_development), Toast.LENGTH_SHORT).show()
        }

        saveButton.setOnClickListener { saveProfile() }
        loadProfile()
    }

    private fun loadProfile() {
        FirebaseSync.fetchEditableProfile(this) { profile ->
            runOnUiThread {
                val firstName = normalizeNamePart(profile?.firstName.orEmpty())
                val lastName = normalizeNamePart(profile?.lastName.orEmpty())
                firstNameInput.setText(firstName)
                lastNameInput.setText(lastName)
                photoBase64 = profile?.photoBase64
                renderAvatar(firstName, photoBase64)
            }
        }
    }

    private fun saveProfile() {
        val firstName = normalizeNamePart(firstNameInput.text?.toString().orEmpty())
        val lastName = normalizeNamePart(lastNameInput.text?.toString().orEmpty())
        firstNameInput.setText(firstName)
        lastNameInput.setText(lastName)

        saveButton.isEnabled = false
        saveButton.isEnabled = true
        Toast.makeText(this, getString(R.string.settings_saved), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun applyBitmap(source: Bitmap) {
        val resized = resizeBitmap(source, 512)
        val output = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 72, output)
        photoBase64 = Base64.encodeToString(output.toByteArray(), Base64.NO_WRAP)
        renderAvatar(firstNameInput.text?.toString().orEmpty(), photoBase64)
    }

    private fun renderAvatar(firstName: String, photo: String?) {
        val initial = firstName.firstOrNull()?.uppercaseChar()?.toString() ?: "U"
        avatarInitial.text = initial
        if (photo.isNullOrBlank()) {
            avatarImage.visibility = View.GONE
            avatarInitial.visibility = View.VISIBLE
            return
        }

        val bytes = try {
            Base64.decode(photo, Base64.DEFAULT)
        } catch (_: Exception) {
            null
        }
        if (bytes == null) {
            avatarImage.visibility = View.GONE
            avatarInitial.visibility = View.VISIBLE
            return
        }
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        if (bitmap == null) {
            avatarImage.visibility = View.GONE
            avatarInitial.visibility = View.VISIBLE
            return
        }
        avatarImage.setImageBitmap(bitmap)
        avatarImage.visibility = View.VISIBLE
        avatarInitial.visibility = View.GONE
    }

    private fun normalizeNamePart(input: String): String {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return trimmed
        return trimmed.lowercase().replaceFirstChar { ch -> ch.titlecase() }
    }

    private fun resizeBitmap(source: Bitmap, maxSize: Int): Bitmap {
        val width = source.width
        val height = source.height
        if (width <= maxSize && height <= maxSize) return source
        val scale = minOf(maxSize.toFloat() / width, maxSize.toFloat() / height)
        val targetW = (width * scale).toInt().coerceAtLeast(1)
        val targetH = (height * scale).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(source, targetW, targetH, true)
    }
}

data class EditableProfile(
    val firstName: String,
    val lastName: String,
    val photoBase64: String?
)
