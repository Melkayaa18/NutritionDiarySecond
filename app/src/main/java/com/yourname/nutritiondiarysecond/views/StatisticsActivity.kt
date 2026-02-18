package com.yourname.nutritiondiarysecond.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.models.DailyChallenge
//аа
class StatisticsActivity : AppCompatActivity() {

    private lateinit var todayCaloriesLabel: TextView
    private lateinit var todayCaloriesProgressBar: ProgressBar
    private lateinit var todayProteinProgressBar: ProgressBar
    private lateinit var todayFatProgressBar: ProgressBar
    private lateinit var todayCarbsProgressBar: ProgressBar
    private lateinit var todayProteinLabel: TextView
    private lateinit var todayFatLabel: TextView
    private lateinit var todayCarbsLabel: TextView
    private lateinit var todayCaloriesPercent: TextView
    private lateinit var weekStatsLabel: TextView
    private lateinit var challengesInfoLabel: TextView
    private lateinit var challengesProgressBar: ProgressBar
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
        // Калории
        todayCaloriesLabel = findViewById(R.id.todayCaloriesLabel)
        todayCaloriesProgressBar = findViewById(R.id.todayCaloriesProgressBar)
        todayCaloriesPercent = findViewById(R.id.todayCaloriesPercent)

        // БЖУ
        todayProteinProgressBar = findViewById(R.id.todayProteinProgressBar)
        todayFatProgressBar = findViewById(R.id.todayFatProgressBar)
        todayCarbsProgressBar = findViewById(R.id.todayCarbsProgressBar)
        todayProteinLabel = findViewById(R.id.todayProteinLabel)
        todayFatLabel = findViewById(R.id.todayFatLabel)
        todayCarbsLabel = findViewById(R.id.todayCarbsLabel)

        // Челленджи и статистика
        weekStatsLabel = findViewById(R.id.weekStatsLabel)
        challengesInfoLabel = findViewById(R.id.challengesInfoLabel)
        challengesProgressBar = findViewById(R.id.challengesProgressBar)
        challengesLayout = findViewById(R.id.challengesLayout)
        achievementsButton = findViewById(R.id.achievementsButton)
    }

    private fun setupClickListeners() {
        achievementsButton.setOnClickListener {
            showDailyTip()
        }
    }

    private fun loadStatistics() {
        // Временные данные
        val calories = 850.0
        val protein = 45.0
        val fat = 30.0
        val carbs = 120.0
        val goal = 2000

        // Обновляем текстовые поля
        todayCaloriesLabel.text = "Съедено: ${calories.toInt()}/$goal ккал"
        todayCaloriesPercent.text = "${(calories / goal * 100).toInt()}%"
        todayProteinLabel.text = "${protein.toInt()}/50г"
        todayFatLabel.text = "${fat.toInt()}/40г"
        todayCarbsLabel.text = "${carbs.toInt()}/200г"

        // Устанавливаем прогресс в ProgressBar
        todayCaloriesProgressBar.progress = (calories / goal * 100).toInt()
        todayProteinProgressBar.progress = (protein / 50 * 100).toInt()
        todayFatProgressBar.progress = (fat / 40 * 100).toInt()
        todayCarbsProgressBar.progress = (carbs / 200 * 100).toInt()

        // Статистика за неделю
        weekStatsLabel.text = """
            📊 Статистика за последние 7 дней:
            
            • Среднее потребление калорий: 1800 ккал/день
            • Самый калорийный день: Понедельник (2100 ккал)
            • Дней в норме: 5 из 7
            • Общий баланс БЖУ: Хороший
        """.trimIndent()
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
        challengesProgressBar.progress = progressPercent

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

            // Используем правильные цвета
            setCardBackgroundColor(
                if (challenge.isCompleted) ContextCompat.getColor(context, R.color.challenge_completed)
                else ContextCompat.getColor(context, R.color.challenge_pending)
            )
            radius = 16f
            cardElevation = 4f
            strokeColor = if (challenge.isCompleted) ContextCompat.getColor(context, R.color.challenge_completed_border)
            else ContextCompat.getColor(context, R.color.challenge_pending_border)
            strokeWidth = 1

            setContentPadding(20, 16, 20, 16)

            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_VERTICAL
            }

            // Иконка
            val iconTextView = TextView(context).apply {
                text = challenge.icon ?: "✅"
                textSize = 20f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = 16
                }
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
                textSize = 16f
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
                if (challenge.isCompleted) {
                    setTypeface(typeface, android.graphics.Typeface.NORMAL)
                } else {
                    setTypeface(typeface, android.graphics.Typeface.BOLD)
                }
            }
            textLayout.addView(titleTextView)

            val descTextView = TextView(context).apply {
                text = challenge.description
                textSize = 12f
                setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                setPadding(0, 4, 0, 0)
            }
            textLayout.addView(descTextView)

            layout.addView(textLayout)

            // Чекбокс
            val checkBox = CheckBox(context).apply {
                isChecked = challenge.isCompleted
                setButtonTintList(android.content.res.ColorStateList.valueOf(
                    ContextCompat.getColor(context, android.R.color.holo_green_dark)
                ))
                setOnCheckedChangeListener { _, isChecked ->
                    val challengeIndex = todayChallenges.indexOfFirst { it.challengeId == challenge.challengeId }
                    if (challengeIndex != -1) {
                        todayChallenges[challengeIndex] = challenge.copy(isCompleted = isChecked)
                        displayChallenges()
                    }
                }
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginStart = 8
                }
            }
            layout.addView(checkBox)

            addView(layout)
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