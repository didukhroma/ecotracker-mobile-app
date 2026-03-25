package com.example.ecotracker

import android.os.Bundle
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
            row.findViewById<ImageView>(R.id.tipImage).setImageResource(tip.imageRes)
            row.findViewById<TextView>(R.id.tipTitle).text = tip.title
            row.findViewById<TextView>(R.id.tipDescription).text = tip.description
            row.findViewById<CheckBox>(R.id.tipCheckbox).isChecked = tip.selected
            tipsContainer.addView(row)
        }
    }
}

data class PersonalTipItem(
    val title: String,
    val description: String,
    val imageRes: Int,
    val selected: Boolean
)

object PersonalTipsRepository {
    // Integration point for server-provided personalized recommendations.
    fun getTips(): List<PersonalTipItem> {
        return listOf(
            PersonalTipItem(
                title = "Transport",
                description = "Go to joint trips (Carpooling) or use public transport.",
                imageRes = android.R.drawable.ic_menu_directions,
                selected = true
            ),
            PersonalTipItem(
                title = "Energy consumption",
                description = "Install energy efficient lamps (such as LED lamps) instead of ordinary ones.",
                imageRes = android.R.drawable.ic_menu_manage,
                selected = false
            ),
            PersonalTipItem(
                title = "Food",
                description = "Reduce meat consumption, especially red, and add more plant foods to your diet.",
                imageRes = android.R.drawable.ic_menu_crop,
                selected = false
            ),
            PersonalTipItem(
                title = "Shopping",
                description = "Avoid single-use plastic products.",
                imageRes = android.R.drawable.ic_menu_delete,
                selected = false
            ),
            PersonalTipItem(
                title = "Water",
                description = "Install aerators on faucets and reduce water usage.",
                imageRes = android.R.drawable.ic_menu_gallery,
                selected = false
            )
        )
    }
}
