package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.card.MaterialCardView
import com.yourname.nutritiondiarysecond.R
//import com.yourname.nutritiondiarysecond.database.NutritionRepository
//import com.yourname.nutritiondiarysecond.database.dao.DiaryEntryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var currentDate: TextView
    private lateinit var calorieProgressBar: android.widget.ProgressBar
    private lateinit var calorieLabel: TextView
    private lateinit var proteinLabel: TextView
    private lateinit var fatLabel: TextView
    private lateinit var carbsLabel: TextView
    private lateinit var trafficLightFrame: MaterialCardView

    // Быстрое добавление
    private lateinit var breakfastCard: MaterialCardView
    private lateinit var lunchCard: MaterialCardView
    private lateinit var dinnerCard: MaterialCardView
    private lateinit var snackCard: MaterialCardView
    private lateinit var waterCard: MaterialCardView
    private lateinit var scannerCard: MaterialCardView

    // Основная навигация
    private lateinit var statisticsCard: MaterialCardView
    private lateinit var recipesCard: MaterialCardView
    private lateinit var productsCard: MaterialCardView
    private lateinit var settingsCard: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupClickListeners()
        loadDailyProgress()
        updateCurrentDate()
    }

    private fun initializeViews() {
        currentDate = findViewById(R.id.currentDate)
        calorieProgressBar = findViewById(R.id.calorieProgressBar)
        calorieLabel = findViewById(R.id.calorieLabel)
        proteinLabel = findViewById(R.id.proteinLabel)
        fatLabel = findViewById(R.id.fatLabel)
        carbsLabel = findViewById(R.id.carbsLabel)
        trafficLightFrame = findViewById(R.id.trafficLightFrame)

        breakfastCard = findViewById(R.id.breakfastButton)
        lunchCard = findViewById(R.id.lunchButton)
        dinnerCard = findViewById(R.id.dinnerButton)
        snackCard = findViewById(R.id.snackButton)
        waterCard = findViewById(R.id.waterButton)
        scannerCard = findViewById(R.id.scannerButton)

        statisticsCard = findViewById(R.id.statisticsButton)
        recipesCard = findViewById(R.id.recipesButton)
        productsCard = findViewById(R.id.productsButton)
        settingsCard = findViewById(R.id.settingsButton)
    }

    private fun setupClickListeners() {
        // Проверка на null для отладки
        breakfastCard.setOnClickListener { openDiaryEntry(1, "Завтрак") }
        lunchCard.setOnClickListener { openDiaryEntry(2, "Обед") }
        dinnerCard.setOnClickListener { openDiaryEntry(3, "Ужин") }
        snackCard.setOnClickListener { openDiaryEntry(4, "Перекус") }

        waterCard.setOnClickListener {
            startActivity(Intent(this, WaterTrackingActivity::class.java))
        }

        scannerCard.setOnClickListener {
            startActivity(Intent(this, BarcodeScannerActivity::class.java))
        }

        statisticsCard.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }

        recipesCard.setOnClickListener {
            startActivity(Intent(this, RecipesActivity::class.java))
        }

        productsCard.setOnClickListener {
            Toast.makeText(this, "База продуктов - скоро будет доступно", Toast.LENGTH_SHORT).show()
        }

        settingsCard.setOnClickListener {
            Toast.makeText(this, "Настройки - скоро будут доступны", Toast.LENGTH_SHORT).show()
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
        // Заглушка - статические данные
        val calories = 850.0
        val protein = 45.0
        val fat = 30.0
        val carbs = 120.0
        val goal = 2000
        val progress = (calories / goal * 100).toInt()
        calorieProgressBar.progress = progress
        calorieLabel.text = "${calories.toInt()}/$goal ккал"
        proteinLabel.text = "${protein.toInt()}г"
        fatLabel.text = "${fat.toInt()}г"
        carbsLabel.text = "${carbs.toInt()}г"
        updateTrafficLight(progress)
    }

    private fun updateTrafficLight(progress: Int) {
        val color = when {
            progress < 50 -> Color.GREEN
            progress < 80 -> Color.YELLOW
            progress < 100 -> Color.rgb(255, 165, 0) // Оранжевый
            else -> Color.RED
        }
        trafficLightFrame.setCardBackgroundColor(color)
    }

    private fun updateCurrentDate() {
        val dateFormat = SimpleDateFormat("EEEE, d MMMM", Locale("ru"))
        val currentDateFormatted = dateFormat.format(Date())
        currentDate.text = currentDateFormatted.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}