package com.yourname.nutritiondiarysecond.views

import android.app.AlertDialog
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.models.WaterIntake
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator
import java.text.SimpleDateFormat
import java.util.*

class WaterTrackingActivity : AppCompatActivity() {
    //аа
    private lateinit var waterProgressText: TextView
    private lateinit var waterProgressFill: ImageView
    private lateinit var waterCenterText: TextView
    private lateinit var add100mlButton: com.google.android.material.button.MaterialButton
    private lateinit var add250mlButton: com.google.android.material.button.MaterialButton
    private lateinit var add500mlButton: com.google.android.material.button.MaterialButton
    private lateinit var customAmountButton: com.google.android.material.button.MaterialButton
    private lateinit var waterHistoryLayout: LinearLayout
    private lateinit var emptyHistory: LinearLayout
    private lateinit var averageDailyText: TextView
    private lateinit var daysWithGoalText: TextView
    private lateinit var weeklyProgressText: TextView
    private lateinit var weeklyProgressBar: LinearProgressIndicator
    private lateinit var todayDateText: TextView
    private lateinit var chartPlaceholder: TextView

    private lateinit var sharedPreferences: SharedPreferences
    private val waterHistory = mutableListOf<WaterIntake>()
    private var totalWaterIntake = 0.0
    private val dailyGoal = 2000.0 // мл

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_tracking)

        sharedPreferences = getSharedPreferences("NutritionDiary", MODE_PRIVATE)
        initializeViews()
        setupClickListeners()
        loadWaterData()
        updateDisplay()
    }

    private fun initializeViews() {
        waterProgressText = findViewById(R.id.waterProgressText)
        waterProgressFill = findViewById(R.id.waterProgressFill)
        waterCenterText = findViewById(R.id.waterCenterText)
        add100mlButton = findViewById(R.id.add100mlButton)
        add250mlButton = findViewById(R.id.add250mlButton)
        add500mlButton = findViewById(R.id.add500mlButton)
        customAmountButton = findViewById(R.id.customAmountButton)
        waterHistoryLayout = findViewById(R.id.waterHistoryLayout)
        emptyHistory = findViewById(R.id.emptyHistory)
        averageDailyText = findViewById(R.id.averageDailyText)
        daysWithGoalText = findViewById(R.id.daysWithGoalText)
        weeklyProgressText = findViewById(R.id.weeklyProgressText)
        weeklyProgressBar = findViewById(R.id.weeklyProgressBar)
        todayDateText = findViewById(R.id.todayDateText)
        chartPlaceholder = findViewById(R.id.chartPlaceholder)

        // Устанавливаем сегодняшнюю дату
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        todayDateText.text = dateFormat.format(Date())
    }

    private fun setupClickListeners() {
        add100mlButton.setOnClickListener { addWater(100.0) }
        add250mlButton.setOnClickListener { addWater(250.0) }
        add500mlButton.setOnClickListener { addWater(500.0) }

        customAmountButton.setOnClickListener {
            showCustomAmountDialog()
        }

        // Делаем текст прогресса кликабельным для сброса
        waterProgressText.setOnClickListener {
            showResetDialog()
        }
    }

    private fun loadWaterData() {
        // Загружаем данные из SharedPreferences
        totalWaterIntake = sharedPreferences.getFloat("water_intake_${getCurrentDateKey()}", 0f).toDouble()

        // Загружаем историю (упрощенно)
        if (totalWaterIntake > 0) {
            // Если есть общее количество, создаем фиктивную запись
            // В реальном приложении нужно загружать историю из БД
            waterHistory.add(WaterIntake(
                waterId = 1,
                userId = getCurrentUserId(),
                intakeDate = Date(),
                amount = totalWaterIntake,
                createdAt = Date()
            ))
        }

        loadWeeklyStats()
    }

    private fun getCurrentDateKey(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun getCurrentUserId(): Int {
        return sharedPreferences.getInt("userId", 0)
    }

    private fun addWater(amount: Double) {
        totalWaterIntake += amount

        // Добавляем в историю
        val newIntake = WaterIntake(
            waterId = generateWaterId(),
            userId = getCurrentUserId(),
            intakeDate = Date(),
            amount = amount,
            createdAt = Date()
        )
        waterHistory.add(newIntake)

        // Сохраняем в SharedPreferences
        saveWaterData()
        updateDisplay()

        // Показываем подтверждение
        val message = when (amount) {
            100.0 -> "Добавлен стакан воды (100 мл)"
            250.0 -> "Добавлена кружка воды (250 мл)"
            500.0 -> "Добавлена бутылка воды (500 мл)"
            else -> "Добавлено ${amount.toInt()} мл воды"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showResetDialog() {
        AlertDialog.Builder(this)
            .setTitle("Сброс трекера воды")
            .setMessage("Вы действительно хотите сбросить потребление воды за сегодня?")
            .setPositiveButton("Сбросить") { dialog, which ->
                totalWaterIntake = 0.0
                waterHistory.clear()
                saveWaterData()
                updateDisplay()
                Toast.makeText(this, "Трекер воды сброшен", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showCustomAmountDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom_water, null)
        val amountInput = dialogView.findViewById<EditText>(R.id.amountInput)

        // Устанавливаем подсказку
        amountInput.hint = "Например: 300"

        val dialog = AlertDialog.Builder(this)
            .setTitle("Добавить воду")
            .setView(dialogView)
            .setPositiveButton("Добавить") { dialog, which ->
                val amountText = amountInput.text.toString()
                if (amountText.isNotEmpty()) {
                    val amount = amountText.toDoubleOrNull()
                    if (amount != null && amount > 0) {
                        if (amount <= 5000) { // Максимум 5 литров за раз
                            addWater(amount)
                        } else {
                            Toast.makeText(this, "Слишком большое количество", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Введите корректное количество", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()

        // Фокус на поле ввода
        amountInput.requestFocus()
    }

    private fun generateWaterId(): Int {
        return (100000..999999).random()
    }

    private fun saveWaterData() {
        sharedPreferences.edit()
            .putFloat("water_intake_${getCurrentDateKey()}", totalWaterIntake.toFloat())
            .apply()
    }

    private fun updateDisplay() {
        // Обновляем текстовый прогресс
        waterProgressText.text = "${totalWaterIntake.toInt()}/${dailyGoal.toInt()} мл"

        // Обновляем процент в центре
        val progressPercent = (totalWaterIntake / dailyGoal * 100).toInt().coerceIn(0, 100)
        waterCenterText.text = "$progressPercent%"

        // Меняем текст под процентом
        val progressText = when {
            progressPercent == 0 -> "начните пить воду"
            progressPercent < 50 -> "цель достижения"
            progressPercent < 100 -> "почти у цели"
            else -> "цель достигнута! 🎉"
        }

        // Находим текстовый view под процентом и обновляем его
        val progressSubtext = waterCenterText.parent as? LinearLayout
        progressSubtext?.findViewById<TextView>(R.id.progressSubtext)?.text = progressText

        // Обновляем визуальный прогресс
        updateProgressVisual(progressPercent)

        // Обновляем историю
        updateWaterHistory()

        // Обновляем статистику
        updateWeeklyStats()
    }

    private fun updateProgressVisual(progressPercent: Int) {
        // Анимация масштаба (от 0 до 1)
        val scale = progressPercent / 100f

        // Используем анимацию для плавного изменения
        waterProgressFill.animate()
            .scaleY(scale)
            .scaleX(scale)
            .setDuration(300)
            .start()

        // Меняем цвет в зависимости от прогресса (совместимый с API 21)
        val color = when {
            progressPercent < 50 -> ContextCompat.getColor(this, android.R.color.holo_blue_light)
            progressPercent < 100 -> ContextCompat.getColor(this, android.R.color.holo_blue_dark)
            else -> ContextCompat.getColor(this, android.R.color.holo_green_dark)
        }
        waterProgressFill.setColorFilter(color)

        // Также меняем цвет центрального текста
        waterCenterText.setTextColor(color)
    }

    private fun updateWaterHistory() {
        waterHistoryLayout.removeAllViews()

        if (waterHistory.isEmpty()) {
            emptyHistory.visibility = android.view.View.VISIBLE
            waterHistoryLayout.visibility = android.view.View.GONE
        } else {
            emptyHistory.visibility = android.view.View.GONE
            waterHistoryLayout.visibility = android.view.View.VISIBLE

            // Показываем последние 10 записей в обратном порядке (новые сверху)
            waterHistory.takeLast(10).reversed().forEachIndexed { index, intake ->
                val historyItem = createHistoryItem(intake, index)
                waterHistoryLayout.addView(historyItem)
            }
        }
    }

    private fun createHistoryItem(intake: WaterIntake, index: Int): MaterialCardView {
        return MaterialCardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 8)
            }

            // Используем контекстно-совместимый цвет
            setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light))
            radius = 12f
            cardElevation = 2f
            strokeWidth = 1
            strokeColor = ContextCompat.getColor(context, android.R.color.darker_gray)

            setContentPadding(16, 12, 16, 12)

            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_VERTICAL
            }

            // Номер записи
            val numberTextView = TextView(context).apply {
                text = "${index + 1}."
                textSize = 14f
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
                setTypeface(typeface, android.graphics.Typeface.BOLD)
                setPadding(0, 0, 16, 0)
            }
            layout.addView(numberTextView)

            // Иконка
            val iconTextView = TextView(context).apply {
                text = "💧"
                textSize = 20f
                setPadding(0, 0, 16, 0)
            }
            layout.addView(iconTextView)

            // Текст
            val textLayout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            val amountTextView = TextView(context).apply {
                text = "+${intake.amount.toInt()} мл"
                textSize = 16f
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            }
            textLayout.addView(amountTextView)

            val timeTextView = TextView(context).apply {
                text = formatTime(intake.createdAt ?: Date())
                textSize = 12f
                setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                setPadding(0, 4, 0, 0)
            }
            textLayout.addView(timeTextView)

            layout.addView(textLayout)

            addView(layout)
        }
    }

    private fun formatTime(date: Date): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return "в ${timeFormat.format(date)}"
    }

    private fun loadWeeklyStats() {
        // Загружаем статистику из SharedPreferences
        val weeklyAverage = sharedPreferences.getFloat("weekly_average", 1800f)
        val daysWithGoal = sharedPreferences.getInt("days_with_goal", 4)
        val totalDays = 7

        // Обновляем UI
        averageDailyText.text = "${weeklyAverage.toInt()} мл"
        daysWithGoalText.text = "$daysWithGoal из $totalDays"

        val progressPercent = (daysWithGoal * 100 / totalDays)
        weeklyProgressBar.progress = progressPercent
        weeklyProgressText.text = "$daysWithGoal/$totalDays дней"

        // Обновляем цвет прогресс-бара
        val progressColor = when {
            progressPercent < 50 -> ContextCompat.getColor(this, android.R.color.holo_red_light)
            progressPercent < 80 -> ContextCompat.getColor(this, android.R.color.holo_orange_light)
            else -> ContextCompat.getColor(this, android.R.color.holo_green_light)
        }
        weeklyProgressBar.setIndicatorColor(progressColor)

        // Устанавливаем текст в заглушку графика
        chartPlaceholder.text = when {
            progressPercent < 30 -> "📉 Нужно пить больше воды"
            progressPercent < 70 -> "📊 Прогресс стабильный"
            else -> "📈 Отличные результаты!"
        }
    }

    private fun updateWeeklyStats() {
        // Здесь будет обновление статистики на основе новых данных
        // Пока просто перезагружаем
        loadWeeklyStats()
    }
}