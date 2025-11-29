package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.yourname.nutritiondiarysecond.R

class MainActivity : AppCompatActivity() {

    private lateinit var calorieProgressBar: ProgressBar
    private lateinit var calorieLabel: TextView
    private lateinit var proteinLabel: TextView
    private lateinit var fatLabel: TextView
    private lateinit var carbsLabel: TextView
    private lateinit var trafficLightFrame: com.google.android.material.card.MaterialCardView

    private lateinit var breakfastButton: Button
    private lateinit var lunchButton: Button
    private lateinit var dinnerButton: Button
    private lateinit var snackButton: Button
    private lateinit var statisticsButton: Button
    private lateinit var recipesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupClickListeners()
        loadDailyProgress()
    }

    private fun initializeViews() {
        calorieProgressBar = findViewById(R.id.calorieProgressBar)
        calorieLabel = findViewById(R.id.calorieLabel)
        proteinLabel = findViewById(R.id.proteinLabel)
        fatLabel = findViewById(R.id.fatLabel)
        carbsLabel = findViewById(R.id.carbsLabel)
        trafficLightFrame = findViewById(R.id.trafficLightFrame)

        breakfastButton = findViewById(R.id.breakfastButton)
        lunchButton = findViewById(R.id.lunchButton)
        dinnerButton = findViewById(R.id.dinnerButton)
        snackButton = findViewById(R.id.snackButton)
        statisticsButton = findViewById(R.id.statisticsButton)
        recipesButton = findViewById(R.id.recipesButton)
    }

    private fun setupClickListeners() {
        breakfastButton.setOnClickListener { openDiaryEntry(1, "Завтрак") }
        lunchButton.setOnClickListener { openDiaryEntry(2, "Обед") }
        dinnerButton.setOnClickListener { openDiaryEntry(3, "Ужин") }
        snackButton.setOnClickListener { openDiaryEntry(4, "Перекус") }

        statisticsButton.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }

        recipesButton.setOnClickListener {
            val intent = Intent(this, RecipesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openDiaryEntry(mealTypeId: Int, mealTypeName: String) {
        val intent = Intent(this, DiaryEntryActivity::class.java).apply {
            putExtra("MEAL_TYPE_ID", mealTypeId)
            putExtra("MEAL_TYPE_NAME", mealTypeName)
        }
        startActivity(intent)
    }

    private fun loadDailyProgress() {
        // Заглушка - в реальном приложении здесь будет загрузка данных
        val calories = 850.0
        val protein = 45.0
        val fat = 30.0
        val carbs = 120.0
        val goal = 2000

        val progress = (calories / goal * 100).toInt()
        calorieProgressBar.progress = progress
        calorieLabel.text = "Съедено: ${calories.toInt()}/$goal ккал"

        proteinLabel.text = "Б: ${protein.toInt()}г"
        fatLabel.text = "Ж: ${fat.toInt()}г"
        carbsLabel.text = "У: ${carbs.toInt()}г"

        // Обновляем цвет светофора
        when {
            progress < 80 -> trafficLightFrame.setCardBackgroundColor(Color.GREEN)
            progress < 100 -> trafficLightFrame.setCardBackgroundColor(Color.YELLOW)
            else -> trafficLightFrame.setCardBackgroundColor(Color.RED)
        }
    }

}