package com.example.ecotracker

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class LearningLessonActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LESSON_ID = "extra_lesson_id"
        const val EXTRA_LESSON_INDEX = "extra_lesson_index"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning_lesson)

        val lessonId = intent.getStringExtra(EXTRA_LESSON_ID).orEmpty()
        val lessonIndex = intent.getIntExtra(EXTRA_LESSON_INDEX, 1)
        val lesson = LearningRepository.getLesson(lessonId) ?: return

        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }
        findViewById<TextView>(R.id.lessonNumberText).text = getString(R.string.lesson_number_format_with_index, lessonIndex)
        findViewById<TextView>(R.id.lessonTitleText).text = lesson.title
        findViewById<TextView>(R.id.lessonContentText).text = lesson.content
        findViewById<ImageView>(R.id.lessonImage).loadAsset(lesson.imageAsset)

        val markButton = findViewById<MaterialButton>(R.id.markCompletedButton)
        val isCompleted = LearningProgressStore.isCompleted(this, lesson.id)
        if (isCompleted) {
            markButton.text = getString(R.string.completed)
            markButton.isEnabled = false
            markButton.alpha = 0.6f
        }

        markButton.setOnClickListener {
            LearningProgressStore.markCompleted(this, lesson.id)
            markButton.text = getString(R.string.completed)
            markButton.isEnabled = false
            markButton.alpha = 0.6f
            Toast.makeText(this, getString(R.string.lesson_completed_saved), Toast.LENGTH_SHORT).show()
        }
    }

    private fun ImageView.loadAsset(assetName: String) {
        assets.open(assetName).use { input ->
            setImageBitmap(BitmapFactory.decodeStream(input))
        }
    }
}
