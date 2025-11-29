package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.models.Recipe

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var recipeTitleEntry: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var descriptionEntry: EditText
    private lateinit var caloriesEntry: EditText
    private lateinit var proteinEntry: EditText
    private lateinit var fatEntry: EditText
    private lateinit var carbsEntry: EditText
    private lateinit var cookingStepsEntry: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var loadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        initializeViews()
        setupClickListeners()
        setupCategorySpinner()
    }

    private fun initializeViews() {
        recipeTitleEntry = findViewById(R.id.recipeTitleEntry)
        categorySpinner = findViewById(R.id.categorySpinner)
        descriptionEntry = findViewById(R.id.descriptionEntry)
        caloriesEntry = findViewById(R.id.caloriesEntry)
        proteinEntry = findViewById(R.id.proteinEntry)
        fatEntry = findViewById(R.id.fatEntry)
        carbsEntry = findViewById(R.id.carbsEntry)
        cookingStepsEntry = findViewById(R.id.cookingStepsEntry)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        loadingIndicator = findViewById(R.id.loadingIndicator)
    }

    private fun setupClickListeners() {
        saveButton.setOnClickListener {
            attemptSaveRecipe()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun setupCategorySpinner() {
        val categories = arrayOf(
            "Завтрак", "Обед", "Ужин", "Перекус", "Десерт", "Напиток", "Салат", "Супы", "Основные блюда"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }

    private fun attemptSaveRecipe() {
        val title = recipeTitleEntry.text.toString().trim()
        val description = descriptionEntry.text.toString().trim()
        val caloriesText = caloriesEntry.text.toString()
        val proteinText = proteinEntry.text.toString()
        val fatText = fatEntry.text.toString()
        val carbsText = carbsEntry.text.toString()
        val cookingSteps = cookingStepsEntry.text.toString().trim()
        val category = categorySpinner.selectedItem as String

        if (!validateInputs(title, caloriesText, proteinText, fatText, carbsText, cookingSteps)) {
            return
        }

        setLoading(true)
        saveRecipe(
            title,
            description,
            category,
            caloriesText.toDouble(),
            proteinText.toDouble(),
            fatText.toDouble(),
            carbsText.toDouble(),
            cookingSteps
        )
    }

    private fun validateInputs(
        title: String,
        caloriesText: String,
        proteinText: String,
        fatText: String,
        carbsText: String,
        cookingSteps: String
    ): Boolean {
        val errors = mutableListOf<String>()

        if (title.isEmpty()) errors.add("Введите название рецепта")
        if (caloriesText.isEmpty() || caloriesText.toDoubleOrNull() == null || caloriesText.toDouble() < 0)
            errors.add("Введите корректную калорийность")
        if (proteinText.isEmpty() || proteinText.toDoubleOrNull() == null || proteinText.toDouble() < 0)
            errors.add("Введите корректное количество белка")
        if (fatText.isEmpty() || fatText.toDoubleOrNull() == null || fatText.toDouble() < 0)
            errors.add("Введите корректное количество жиров")
        if (carbsText.isEmpty() || carbsText.toDoubleOrNull() == null || carbsText.toDouble() < 0)
            errors.add("Введите корректное количество углеводов")
        if (cookingSteps.isEmpty()) errors.add("Введите шаги приготовления")

        if (errors.isNotEmpty()) {
            showError(errors.joinToString("\n"))
            return false
        }

        return true
    }

    private fun saveRecipe(
        title: String,
        description: String,
        category: String,
        calories: Double,
        protein: Double,
        fat: Double,
        carbs: Double,
        cookingSteps: String
    ) {
        // Временная заглушка - сохранение рецепта
        android.os.Handler().postDelayed({
            setLoading(false)

            // В реальном приложении здесь будет вызов API
            val newRecipe = Recipe(
                recipeId = generateRecipeId(),
                title = title,
                description = description,
                category = category,
                caloriesPerServing = calories,
                proteinPerServing = protein,
                fatPerServing = fat,
                carbsPerServing = carbs,
                imagePath = null,
                cookingSteps = cookingSteps,
                isActive = true
            )

            showSuccess("Рецепт \"$title\" успешно создан!")

            // Возвращаемся на предыдущий экран
            val resultIntent = Intent()
            resultIntent.putExtra("newRecipe", newRecipe)
            setResult(RESULT_OK, resultIntent)
            finish()
        }, 1500)
    }

    private fun generateRecipeId(): Int {
        return (100000..999999).random()
    }

    private fun setLoading(loading: Boolean) {
        loadingIndicator.visibility = if (loading) android.view.View.VISIBLE else android.view.View.GONE
        saveButton.isEnabled = !loading
        cancelButton.isEnabled = !loading

        if (loading) {
            saveButton.text = "Сохранение..."
        } else {
            saveButton.text = "Сохранить рецепт"
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}