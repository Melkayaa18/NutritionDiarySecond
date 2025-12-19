package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.yourname.nutritiondiarysecond.R
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var currentDate: TextView
    private lateinit var calorieProgressBar: android.widget.ProgressBar
    private lateinit var calorieLabel: TextView
    private lateinit var proteinLabel: TextView
    private lateinit var fatLabel: TextView
    private lateinit var carbsLabel: TextView
    private lateinit var trafficLightFrame: MaterialCardView

    // Быстрое добавление - теперь это MaterialCardView
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

        // Быстрое добавление - находим как MaterialCardView
        breakfastCard = findViewById(R.id.breakfastButton)
        lunchCard = findViewById(R.id.lunchButton)
        dinnerCard = findViewById(R.id.dinnerButton)
        snackCard = findViewById(R.id.snackButton)
        waterCard = findViewById(R.id.waterButton)
        scannerCard = findViewById(R.id.scannerButton)

        // Основная навигация
        statisticsCard = findViewById(R.id.statisticsButton)
        recipesCard = findViewById(R.id.recipesButton)
        productsCard = findViewById(R.id.productsButton)
        settingsCard = findViewById(R.id.settingsButton)
    }

    private fun setupClickListeners() {
        // Приемы пищи
        breakfastCard.setOnClickListener { openDiaryEntry(1, "Завтрак") }
        lunchCard.setOnClickListener { openDiaryEntry(2, "Обед") }
        dinnerCard.setOnClickListener { openDiaryEntry(3, "Ужин") }
        snackCard.setOnClickListener { openDiaryEntry(4, "Перекус") }

        // Вода и сканер
        waterCard.setOnClickListener {
            val intent = Intent(this, WaterTrackingActivity::class.java)
            startActivity(intent)
        }

        scannerCard.setOnClickListener {
            val intent = Intent(this, BarcodeScannerActivity::class.java)
            startActivity(intent)
        }

        // Основная навигация
        statisticsCard.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }

        recipesCard.setOnClickListener {
            val intent = Intent(this, RecipesActivity::class.java)
            startActivity(intent)
        }

        productsCard.setOnClickListener {
            // Пока заглушка - можно создать ProductsActivity позже
            android.widget.Toast.makeText(this, "База продуктов - скоро будет доступно", android.widget.Toast.LENGTH_SHORT).show()
        }

        settingsCard.setOnClickListener {
            // Пока заглушка
            android.widget.Toast.makeText(this, "Настройки - скоро будут доступны", android.widget.Toast.LENGTH_SHORT).show()
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
        calorieLabel.text = "${calories.toInt()}/$goal ккал"

        proteinLabel.text = "${protein.toInt()}г"
        fatLabel.text = "${fat.toInt()}г"
        carbsLabel.text = "${carbs.toInt()}г"

        // Обновляем цвет светофора
        updateTrafficLight(progress)
    }

    private fun updateTrafficLight(progress: Int) {
        val (color, status) = when {
            progress < 50 -> Color.GREEN to "Норма"
            progress < 80 -> Color.YELLOW to "Средне"
            progress < 100 -> Color.rgb(255, 165, 0) to "Много" // Оранжевый
            else -> Color.RED to "Перебор"
        }
        trafficLightFrame.setCardBackgroundColor(color)
    }

    private fun updateCurrentDate() {
        val dateFormat = SimpleDateFormat("EEEE, d MMMM", Locale("ru"))
        val currentDateFormatted = dateFormat.format(Date())
        currentDate.text = currentDateFormatted.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}