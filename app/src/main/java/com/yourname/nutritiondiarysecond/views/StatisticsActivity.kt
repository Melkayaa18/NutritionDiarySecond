package com.yourname.nutritiondiarysecond.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.models.DailyChallenge

class StatisticsActivity : AppCompatActivity() {

    private lateinit var todayCaloriesLabel: TextView
    private lateinit var todayCaloriesProgressFill: View
    private lateinit var todayProteinProgressFill: View
    private lateinit var todayFatProgressFill: View
    private lateinit var todayCarbsProgressFill: View
    private lateinit var weekStatsLabel: TextView
    private lateinit var challengesInfoLabel: TextView
    private lateinit var challengesProgressFill: View
    private lateinit var challengesLayout: LinearLayout
    private lateinit var achievementsButton: Button

    private val todayChallenges = mutableListOf<DailyChallenge>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        initializeViews()
        setupClickListeners()
        loadStatistics()
        loadDailyChallenges()
    }

    private fun initializeViews() {
        todayCaloriesLabel = findViewById(R.id.todayCaloriesLabel)
        todayCaloriesProgressFill = findViewById(R.id.todayCaloriesProgressFill)
        todayProteinProgressFill = findViewById(R.id.todayProteinProgressFill)
        todayFatProgressFill = findViewById(R.id.todayFatProgressFill)
        todayCarbsProgressFill = findViewById(R.id.todayCarbsProgressFill)
        weekStatsLabel = findViewById(R.id.weekStatsLabel)
        challengesInfoLabel = findViewById(R.id.challengesInfoLabel)
        challengesProgressFill = findViewById(R.id.challengesProgressFill)
        challengesLayout = findViewById(R.id.challengesLayout)
        achievementsButton = findViewById(R.id.achievementsButton)
    }

    private fun setupClickListeners() {
        achievementsButton.setOnClickListener {
            showDailyTip()
        }
    }

    private fun loadStatistics() {
        // Заглушка - временные данные
        val calories = 850.0
        val protein = 45.0
        val fat = 30.0
        val carbs = 120.0
        val goal = 2000

        todayCaloriesLabel.text = "Съедено: ${calories.toInt()}/$goal ккал"

        // Анимируем прогресс-бары
        animateProgressBar(todayCaloriesProgressFill, (calories / goal * 100).toInt())
        animateProgressBar(todayProteinProgressFill, (protein / 50 * 100).toInt())
        animateProgressBar(todayFatProgressFill, (fat / 40 * 100).toInt())
        animateProgressBar(todayCarbsProgressFill, (carbs / 200 * 100).toInt())

        // Статистика за неделю
        weekStatsLabel.text = """
            📊 Статистика за последние 7 дней:
            
            • Среднее потребление калорий: 1800 ккал/день
            • Самый калорийный день: Понедельник (2100 ккал)
            • Дней в норме: 5 из 7
            • Общий баланс БЖУ: Хороший
        """.trimIndent()
    }

    private fun animateProgressBar(progressView: View, progressPercent: Int) {
        // Простая анимация прогресса
        val maxWidth = when (progressView) {
            todayCaloriesProgressFill -> 1000 // Ширина для калорий
            else -> 300 // Ширина для БЖУ
        }

        val targetWidth = (maxWidth * progressPercent / 100).coerceAtMost(maxWidth)

        // Используем post для обновления UI в основном потоке
        progressView.post {
            val layoutParams = progressView.layoutParams
            layoutParams.width = targetWidth
            progressView.layoutParams = layoutParams
        }
    }

    private fun loadDailyChallenges() {
        // Заглушка - создаем тестовые челленджи
        todayChallenges.clear()
        todayChallenges.addAll(listOf(
            DailyChallenge(1, "Выпить 2 литра воды", "Следите за водным балансом", "Питание", false, java.util.Date()),
            DailyChallenge(2, "Утренняя зарядка", "10-15 минут физической активности", "Спорт", true, java.util.Date()),
            DailyChallenge(3, "Ранний подъем", "Проснитесь на 30 минут раньше", "Здоровье", false, java.util.Date())
        ))

        displayChallenges()
    }

    private fun displayChallenges() {
        challengesLayout.removeAllViews()

        if (todayChallenges.isEmpty()) {
            challengesInfoLabel.text = "Нет активных челленджей"
            return
        }

        val completedCount = todayChallenges.count { it.isCompleted }
        val progressPercent = (completedCount.toDouble() / todayChallenges.size * 100).toInt()

        challengesInfoLabel.text = "$completedCount/${todayChallenges.size}"

        // Анимируем прогресс челленджей
        animateProgressBar(challengesProgressFill, progressPercent)

        todayChallenges.forEach { challenge ->
            val challengeView = createChallengeView(challenge)
            challengesLayout.addView(challengeView)
        }
    }

    private fun createChallengeView(challenge: DailyChallenge): com.google.android.material.card.MaterialCardView {
        return com.google.android.material.card.MaterialCardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 12)
            }

            // Используем стандартные цвета, пока не создадим свои
            setCardBackgroundColor(
                if (challenge.isCompleted) ContextCompat.getColor(context, android.R.color.holo_green_light)
                else ContextCompat.getColor(context, android.R.color.holo_orange_light)
            )
            radius = 12f
            cardElevation = 4f
            strokeColor = if (challenge.isCompleted) ContextCompat.getColor(context, android.R.color.holo_green_dark)
            else ContextCompat.getColor(context, android.R.color.holo_orange_dark)
            strokeWidth = 2

            setContentPadding(20, 20, 20, 20)

            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 0, 0, 0)
            }

            // Иконка
            val iconTextView = TextView(context).apply {
                text = challenge.icon
                textSize = 20f
                setPadding(0, 0, 15, 0)
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

            val titleTextView = TextView(context).apply {
                text = challenge.title
                textSize = 14f
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
                if (challenge.isCompleted) {
                    setTypeface(typeface, android.graphics.Typeface.ITALIC)
                } else {
                    setTypeface(typeface, android.graphics.Typeface.BOLD)
                }
            }
            textLayout.addView(titleTextView)

            val descTextView = TextView(context).apply {
                text = challenge.description
                textSize = 12f
                setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                setPadding(0, 5, 0, 0)
            }
            textLayout.addView(descTextView)

            val categoryTextView = TextView(context).apply {
                text = challenge.category
                textSize = 10f
                setTextColor(getCategoryColor(challenge.category))
                setTypeface(typeface, android.graphics.Typeface.BOLD)
                setPadding(0, 5, 0, 0)
            }
            textLayout.addView(categoryTextView)

            layout.addView(textLayout)

            // Чекбокс - ИСПРАВЛЕННАЯ ЧАСТЬ
            val checkBox = CheckBox(context).apply {
                isChecked = challenge.isCompleted
                setButtonTintList(android.content.res.ColorStateList.valueOf(
                    ContextCompat.getColor(context, android.R.color.holo_green_dark)
                ))
                setOnCheckedChangeListener { _, isChecked ->
                    // Находим индекс этого челленджа в списке и обновляем его
                    val challengeIndex = todayChallenges.indexOfFirst { it.challengeId == challenge.challengeId }
                    if (challengeIndex != -1) {
                        todayChallenges[challengeIndex] = challenge.copy(isCompleted = isChecked)
                        displayChallenges() // Обновляем отображение
                    }
                }
            }
            layout.addView(checkBox)

            addView(layout)
        }
    }

    private fun getCategoryColor(category: String): Int {
        return when (category) {
            "Питание" -> ContextCompat.getColor(this, android.R.color.holo_orange_dark)
            "Спорт" -> ContextCompat.getColor(this, android.R.color.holo_blue_dark)
            "Здоровье" -> ContextCompat.getColor(this, android.R.color.holo_purple)
            else -> ContextCompat.getColor(this, android.R.color.darker_gray)
        }
    }

    private fun showDailyTip() {
        val tips = listOf(
            "💡 Пейте воду перед едой - это поможет съесть меньше",
            "💡 10-минутная прогулка после еды улучшает пищеварение",
            "💡 Здоровый сон - ключ к контролю аппетита",
            "💡 Готовьте еду заранее, чтобы избежать вредных перекусов",
            "💡 Медленные приемы пищи помогают лучше чувствовать насыщение",
            "💡 Белок на завтрак помогает контролировать голод в течение дня"
        )

        val randomTip = tips.random()

        Toast.makeText(this, randomTip, Toast.LENGTH_LONG).show()
    }
}