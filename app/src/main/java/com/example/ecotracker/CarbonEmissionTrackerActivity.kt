package com.example.ecotracker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CarbonEmissionTrackerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carbon_emission_tracker)

        findViewById<TextView>(R.id.backLink).setOnClickListener { finish() }
    }
}
