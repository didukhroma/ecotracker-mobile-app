package com.example.ecotracker

import android.os.Bundle
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PersonalTipsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_tips)

        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }

        val tipsContainer: LinearLayout = findViewById(R.id.tipsContainer)
        val tips = PersonalTipsRepository.getTips()
        val inflater = LayoutInflater.from(this)

        tips.forEach { tip ->
            val row = inflater.inflate(R.layout.item_personal_tip, tipsContainer, false)
            bindTipImage(row.findViewById(R.id.tipImage), tip.imageAssetPath)
            row.findViewById<TextView>(R.id.tipTitle).text = tip.title
            row.findViewById<TextView>(R.id.tipDescription).text = tip.description
            row.findViewById<CheckBox>(R.id.tipCheckbox).isChecked = tip.selected
            tipsContainer.addView(row)
        }
    }

    private fun bindTipImage(imageView: ImageView, assetPath: String) {
        val bitmap = try {
            assets.open(assetPath).use { input -> BitmapFactory.decodeStream(input) }
        } catch (_: Exception) {
            null
        }
        imageView.setImageBitmap(bitmap)
    }
}

data class PersonalTipItem(
    val title: String,
    val description: String,
    val imageAssetPath: String,
    val selected: Boolean
)

object PersonalTipsRepository {
    // Integration point for server-provided personalized recommendations.
    fun getTips(): List<PersonalTipItem> {
        return listOf(
            PersonalTipItem(
                title = "Transport",
                description = "Go to joint trips (Carpooling) or use public transport.",
                imageAssetPath = "personal_tips/carpool.png",
                selected = true
            ),
            PersonalTipItem(
                title = "Energy consumption",
                description = "Install energy efficient lamps (such as LED lamps) instead of ordinary ones.",
                imageAssetPath = "personal_tips/energy.jpg",
                selected = false
            ),
            PersonalTipItem(
                title = "Food",
                description = "Reduce meat consumption, especially red, and add more plant foods to your diet.",
                imageAssetPath = "personal_tips/food.jpg",
                selected = false
            ),
            PersonalTipItem(
                title = "Shopping",
                description = "Avoid single-use plastic products.",
                imageAssetPath = "personal_tips/shopping.jpg",
                selected = false
            ),
            PersonalTipItem(
                title = "Water",
                description = "Install aerators on faucets and reduce water usage.",
                imageAssetPath = "personal_tips/water.jpg",
                selected = false
            )
        )
    }
}
