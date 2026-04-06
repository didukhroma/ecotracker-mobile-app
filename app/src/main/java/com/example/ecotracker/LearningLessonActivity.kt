package com.example.ecotracker

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

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
        findViewById<TextView>(R.id.lessonIntroText).text = lesson.introText
        findViewById<TextView>(R.id.sectionOneTitleText).text = lesson.sectionOneTitle
        findViewById<TextView>(R.id.sectionOneBodyText).text = lesson.sectionOneBody
        findViewById<TextView>(R.id.sectionTwoTitleText).text = lesson.sectionTwoTitle
        findViewById<TextView>(R.id.sectionTwoBodyText).text = lesson.sectionTwoBody
        findViewById<TextView>(R.id.teacherNameText).text = getString(R.string.learning_teacher_name)
        findViewById<TextView>(R.id.teacherRoleText).text = getString(R.string.learning_teacher_role)
        findViewById<ImageView>(R.id.teacherImage).loadAsset("teacher.jpg")
        findViewById<ImageView>(R.id.lessonImage).loadAsset(lesson.imageAsset)

        val bottomImageCard = findViewById<MaterialCardView>(R.id.bottomImageCard)
        val bottomImage = findViewById<ImageView>(R.id.bottomImage)
        val bottomCaption = findViewById<TextView>(R.id.bottomCaptionText)
        if (lesson.bottomImageAsset.isNullOrBlank()) {
            bottomImageCard.visibility = android.view.View.GONE
            bottomImage.visibility = android.view.View.GONE
            bottomCaption.visibility = android.view.View.GONE
        } else {
            bottomImageCard.visibility = android.view.View.VISIBLE
            bottomImage.visibility = android.view.View.VISIBLE
            bottomCaption.visibility = android.view.View.VISIBLE
            bottomImage.loadAsset(lesson.bottomImageAsset)
            bottomCaption.text = lesson.bottomCaption.orEmpty()
        }

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
        val bitmap = try {
            assets.open(assetName).use { input -> BitmapFactory.decodeStream(input) }
        } catch (_: Exception) {
            assets.open("home.png").use { input -> BitmapFactory.decodeStream(input) }
        }
        setImageBitmap(bitmap)
    }
}
