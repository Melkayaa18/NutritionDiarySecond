package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.yourname.nutritiondiarysecond.R

class RecipeFilterActivity : AppCompatActivity() {

    private lateinit var categorySpinner: Spinner
    private lateinit var caloriesFrom: EditText
    private lateinit var caloriesTo: EditText
    private lateinit var timeSeekBar: SeekBar
    private lateinit var timeValue: TextView
    private lateinit var difficultyGroup: RadioGroup
    private lateinit var ingredientsEntry: EditText
    private lateinit var applyButton: Button
    private lateinit var resetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_filter)

        initializeViews()
        setupClickListeners()
        setupCategorySpinner()
        setupSeekBar()
    }

    private fun initializeViews() {
        categorySpinner = findViewById(R.id.categorySpinner)
        caloriesFrom = findViewById(R.id.caloriesFrom)
        caloriesTo = findViewById(R.id.caloriesTo)
        timeSeekBar = findViewById(R.id.timeSeekBar)
        timeValue = findViewById(R.id.timeValue)
        difficultyGroup = findViewById(R.id.difficultyGroup)
        ingredientsEntry = findViewById(R.id.ingredientsEntry)
        applyButton = findViewById(R.id.applyButton)
        resetButton = findViewById(R.id.resetButton)
    }

    private fun setupClickListeners() {
        applyButton.setOnClickListener {
            applyFilters()
        }

        resetButton.setOnClickListener {
            resetFilters()
        }
    }

    private fun setupCategorySpinner() {
        val categories = arrayOf(
            "Все категории", "Завтрак", "Обед", "Ужин", "Перекус",
            "Десерт", "Напиток", "Салат", "Супы", "Основные блюда"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }

    private fun setupSeekBar() {
        timeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateTimeText(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        updateTimeText(timeSeekBar.progress)
    }

    private fun updateTimeText(progress: Int) {
        val time = when {
            progress == 0 -> "Любое время"
            progress <= 15 -> "До 15 минут"
            progress <= 30 -> "До 30 минут"
            progress <= 60 -> "До 1 часа"
            else -> "Более 1 часа"
        }
        timeValue.text = time
    }

    private fun applyFilters() {
        val category = if (categorySpinner.selectedItemPosition == 0) null else categorySpinner.selectedItem as String
        val caloriesFromValue = caloriesFrom.text.toString().toIntOrNull()
        val caloriesToValue = caloriesTo.text.toString().toIntOrNull()
        val maxTime = timeSeekBar.progress
        val ingredients = ingredientsEntry.text.toString().trim().takeIf { it.isNotEmpty() }

        val selectedDifficultyId = difficultyGroup.checkedRadioButtonId
        val difficulty = when (selectedDifficultyId) {
            R.id.difficultyEasy -> "Легкая"
            R.id.difficultyMedium -> "Средняя"
            else -> null
        }

        // Переходим к FilteredRecipesActivity с параметрами фильтрации
        val intent = Intent(this, FilteredRecipesActivity::class.java).apply {
            putExtra("category", category)
            putExtra("caloriesFrom", caloriesFromValue)
            putExtra("caloriesTo", caloriesToValue)
            putExtra("maxTime", maxTime)
            putExtra("difficulty", difficulty)
            putExtra("ingredients", ingredients)
        }
        startActivity(intent)
        finish()

        Toast.makeText(this, "Фильтры применены", Toast.LENGTH_SHORT).show()
    }

    private fun resetFilters() {
        categorySpinner.setSelection(0)
        caloriesFrom.setText("")
        caloriesTo.setText("")
        timeSeekBar.progress = 30
        difficultyGroup.check(R.id.difficultyAny)
        ingredientsEntry.setText("")

        Toast.makeText(this, "Фильтры сброшены", Toast.LENGTH_SHORT).show()
    }

}