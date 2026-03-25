package com.example.ecotracker

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AchievementsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        renderAchievements()
    }

    private fun renderAchievements() {
        val states = AchievementsRepository.getStates(this)
        val opened = states.count { it.claimed }
        findViewById<TextView>(R.id.rewardsProgressText).text =
            getString(R.string.rewards_progress_format, opened, AchievementsRepository.totalRewards)

        val recycler = findViewById<RecyclerView>(R.id.achievementsRecycler)
        recycler.layoutManager = GridLayoutManager(this, 3)
        recycler.adapter = AchievementAdapter(states) { state ->
            if (state.unlocked && !state.claimed) {
                startActivity(
                    Intent(this, AchievementReceivedActivity::class.java)
                        .putExtra(AchievementReceivedActivity.EXTRA_ACHIEVEMENT_ID, state.item.id)
                )
            } else {
                startActivity(
                    Intent(this, AchievementDetailActivity::class.java)
                        .putExtra(AchievementDetailActivity.EXTRA_ACHIEVEMENT_ID, state.item.id)
                )
            }
        }
    }

    private inner class AchievementAdapter(
        private val items: List<AchievementState>,
        private val onClick: (AchievementState) -> Unit
    ) : RecyclerView.Adapter<AchievementViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_achievement_tile, parent, false)
            return AchievementViewHolder(view as ViewGroup, onClick)
        }

        override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size
    }

    private inner class AchievementViewHolder(
        private val root: ViewGroup,
        private val clickHandler: (AchievementState) -> Unit
    ) : RecyclerView.ViewHolder(root) {
        private val image: ImageView = root.findViewById(R.id.achievementImage)
        private val title: TextView = root.findViewById(R.id.achievementTitle)

        fun bind(state: AchievementState) {
            image.loadAsset(state.item.imageAsset)
            title.text = state.item.title

            if (!state.unlocked) {
                val matrix = ColorMatrix().apply { setSaturation(0f) }
                image.colorFilter = ColorMatrixColorFilter(matrix)
                image.alpha = 0.45f
            } else {
                image.clearColorFilter()
                image.alpha = 1f
            }

            if (state.claimed) {
                title.alpha = 1f
            } else if (state.unlocked) {
                title.alpha = 0.95f
            } else {
                title.alpha = 0.6f
            }

            root.setOnClickListener { clickHandler(state) }
        }
    }

    private fun ImageView.loadAsset(assetName: String) {
        assets.open(assetName).use { input ->
            setImageBitmap(BitmapFactory.decodeStream(input))
        }
    }
}
