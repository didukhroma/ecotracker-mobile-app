package com.example.ecotracker

import android.content.Context
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
        val tips = PersonalTipsRepository.getTips(this)
        val inflater = LayoutInflater.from(this)

        tips.forEach { tip ->
            val row = inflater.inflate(R.layout.item_personal_tip, tipsContainer, false)
            bindTipImage(row.findViewById(R.id.tipImage), tip.imageAssetPath)
            row.findViewById<TextView>(R.id.tipTitle).text = tip.title
            row.findViewById<TextView>(R.id.tipDescription).text = tip.description
            row.findViewById<CheckBox>(R.id.tipCheckbox).apply {
                isChecked = tip.selected
                setOnCheckedChangeListener { _, isChecked ->
                    PersonalTipsStore.setSelected(this@PersonalTipsActivity, tip.id, isChecked)
                }
            }
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
    val id: String,
    val title: String,
    val description: String,
    val imageAssetPath: String,
    val selected: Boolean
)

object PersonalTipsRepository {
    // Integration point for server-provided personalized recommendations.
    fun getTips(context: Context): List<PersonalTipItem> {
        val selectedIds = PersonalTipsStore.getSelectedIds(context)
        return listOf(
            PersonalTipItem(
                id = "transport_tip",
                title = "Transport",
                description = "Go to joint trips (Carpooling) or use public transport.",
                imageAssetPath = "personal_tips/carpool.png",
                selected = selectedIds.contains("transport_tip")
            ),
            PersonalTipItem(
                id = "energy_tip",
                title = "Energy consumption",
                description = "Install energy efficient lamps (such as LED lamps) instead of ordinary ones.",
                imageAssetPath = "personal_tips/energy.jpg",
                selected = selectedIds.contains("energy_tip")
            ),
            PersonalTipItem(
                id = "food_tip",
                title = "Food",
                description = "Reduce meat consumption, especially red, and add more plant foods to your diet.",
                imageAssetPath = "personal_tips/food.jpg",
                selected = selectedIds.contains("food_tip")
            ),
            PersonalTipItem(
                id = "shopping_tip",
                title = "Shopping",
                description = "Avoid single-use plastic products.",
                imageAssetPath = "personal_tips/shopping.jpg",
                selected = selectedIds.contains("shopping_tip")
            ),
            PersonalTipItem(
                id = "water_tip",
                title = "Water",
                description = "Install aerators on faucets and reduce water usage.",
                imageAssetPath = "personal_tips/water.jpg",
                selected = selectedIds.contains("water_tip")
            ),
            PersonalTipItem(
                id = "trees_tip",
                title = "Trees",
                description = "Choose a native species that fits your local climate and support biodiversity in your area.",
                imageAssetPath = "personal_tips/trees.jpg",
                selected = selectedIds.contains("trees_tip")
            )
        )
    }
}

object PersonalTipsStore {
    private const val PREFS_NAME = "personal_tips_state"
    private const val KEY_SELECTED_IDS = "selected_tip_ids"

    fun getSelectedIds(context: Context): Set<String> {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getStringSet(KEY_SELECTED_IDS, emptySet())
            ?.toSet()
            ?: emptySet()
    }

    fun isSelected(context: Context, tipId: String): Boolean = getSelectedIds(context).contains(tipId)

    fun selectedCount(context: Context): Int = getSelectedIds(context).size

    fun setSelected(context: Context, tipId: String, selected: Boolean) {
        val updated = getSelectedIds(context).toMutableSet().apply {
            if (selected) add(tipId) else remove(tipId)
        }
        persist(context, updated)
        FirebaseSync.syncPersonalTips(context)
    }

    fun setSelectedIds(context: Context, ids: Set<String>) {
        persist(context, ids)
    }

    private fun persist(context: Context, ids: Set<String>) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(KEY_SELECTED_IDS, ids)
            .apply()
    }
}
