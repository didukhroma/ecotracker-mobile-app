package com.example.ecotracker

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LearningPlatformActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning_platform)

        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }
        renderCategories()
    }

    override fun onResume() {
        super.onResume()
        renderCategories()
    }

    private fun renderCategories() {
        val categoriesContainer = findViewById<LinearLayout>(R.id.categoriesContainer)
        categoriesContainer.removeAllViews()

        val categories = LearningRepository.getCategories()
        val inflater = LayoutInflater.from(this)

        categories.forEach { category ->
            val row = inflater.inflate(R.layout.item_learning_category, categoriesContainer, false)
            row.findViewById<TextView>(R.id.categoryTitle).text = category.title
            row.findViewById<TextView>(R.id.categoryProgress).text =
                getString(R.string.learning_progress_format, LearningProgressStore.completedCount(this, category), category.lessons.size)

            val previewContainer = row.findViewById<LinearLayout>(R.id.lessonPreviewContainer)
            category.lessons.take(3).forEachIndexed { index, lesson ->
                val preview = inflater.inflate(R.layout.item_learning_preview_card, previewContainer, false)
                preview.findViewById<TextView>(R.id.previewTitle).text = getString(R.string.lesson_number_format, index + 1)
                preview.findViewById<ImageView>(R.id.previewImage).loadAsset(lesson.imageAsset)
                preview.setOnClickListener { openCategory(category.id) }
                previewContainer.addView(preview)
            }

            row.findViewById<TextView>(R.id.seeAllText).setOnClickListener { openCategory(category.id) }
            row.setOnClickListener { openCategory(category.id) }

            categoriesContainer.addView(row)
        }
    }

    private fun openCategory(categoryId: String) {
        startActivity(Intent(this, LearningCategoryActivity::class.java).putExtra(LearningCategoryActivity.EXTRA_CATEGORY_ID, categoryId))
    }

    private fun ImageView.loadAsset(assetName: String) {
        assets.open(assetName).use { input ->
            setImageBitmap(BitmapFactory.decodeStream(input))
        }
    }
}
