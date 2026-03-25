package com.example.ecotracker

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LearningCategoryActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CATEGORY_ID = "extra_category_id"
    }

    private var categoryId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning_category)

        categoryId = intent.getStringExtra(EXTRA_CATEGORY_ID).orEmpty()
        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        renderCategory()
    }

    private fun renderCategory() {
        val category = LearningRepository.getCategory(categoryId) ?: return

        findViewById<TextView>(R.id.categoryTitleText).text = category.title
        findViewById<TextView>(R.id.categoryProgressText).text =
            getString(R.string.learning_progress_format, LearningProgressStore.completedCount(this, category), category.lessons.size)

        val lessonsContainer = findViewById<LinearLayout>(R.id.lessonsContainer)
        lessonsContainer.removeAllViews()

        val inflater = LayoutInflater.from(this)
        category.lessons.forEachIndexed { index, lesson ->
            val row = inflater.inflate(R.layout.item_learning_lesson_row, lessonsContainer, false)
            row.findViewById<TextView>(R.id.lessonNumberText).text = getString(R.string.lesson_number_format_with_index, index + 1)
            row.findViewById<TextView>(R.id.lessonTitleText).text = lesson.title
            row.findViewById<TextView>(R.id.lessonDescriptionText).text = lesson.shortDescription
            row.findViewById<ImageView>(R.id.lessonImage).loadAsset(lesson.imageAsset)
            row.findViewById<TextView>(R.id.alreadyPassedText).visibility =
                if (LearningProgressStore.isCompleted(this, lesson.id)) android.view.View.VISIBLE else android.view.View.GONE

            row.setOnClickListener {
                startActivity(
                    Intent(this, LearningLessonActivity::class.java)
                        .putExtra(LearningLessonActivity.EXTRA_LESSON_ID, lesson.id)
                        .putExtra(LearningLessonActivity.EXTRA_LESSON_INDEX, index + 1)
                )
            }
            lessonsContainer.addView(row)
        }
    }

    private fun ImageView.loadAsset(assetName: String) {
        assets.open(assetName).use { input ->
            setImageBitmap(BitmapFactory.decodeStream(input))
        }
    }
}
