package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.models.Recipe

class MyRecipeDetailsActivity : AppCompatActivity() {

    private lateinit var recipeTitle: TextView
    private lateinit var recipeCategory: TextView
    private lateinit var caloriesValue: TextView
    private lateinit var proteinValue: TextView
    private lateinit var fatValue: TextView
    private lateinit var carbsValue: TextView
    private lateinit var descriptionValue: TextView
    private lateinit var cookingStepsValue: TextView
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var shareButton: Button

    private lateinit var currentRecipe: Recipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_recipe_details)

        currentRecipe = intent.getParcelableExtra<Recipe>("recipe") ?: return

        initializeViews()
        setupClickListeners()
        displayRecipeDetails()
    }

    private fun initializeViews() {
        recipeTitle = findViewById(R.id.recipeTitle)
        recipeCategory = findViewById(R.id.recipeCategory)
        caloriesValue = findViewById(R.id.caloriesValue)
        proteinValue = findViewById(R.id.proteinValue)
        fatValue = findViewById(R.id.fatValue)
        carbsValue = findViewById(R.id.carbsValue)
        descriptionValue = findViewById(R.id.descriptionValue)
        cookingStepsValue = findViewById(R.id.cookingStepsValue)
        editButton = findViewById(R.id.editButton)
        deleteButton = findViewById(R.id.deleteButton)
        shareButton = findViewById(R.id.shareButton)
    }

    private fun setupClickListeners() {
        editButton.setOnClickListener {
            editRecipe()
        }

        deleteButton.setOnClickListener {
            deleteRecipe()
        }

        shareButton.setOnClickListener {
            shareRecipe()
        }
    }

    private fun displayRecipeDetails() {
        recipeTitle.text = currentRecipe.title
        recipeCategory.text = currentRecipe.category
        caloriesValue.text = "${currentRecipe.caloriesPerServing} ккал"
        proteinValue.text = "${currentRecipe.proteinPerServing} г"
        fatValue.text = "${currentRecipe.fatPerServing} г"
        carbsValue.text = "${currentRecipe.carbsPerServing} г"
        descriptionValue.text = currentRecipe.description
        cookingStepsValue.text = currentRecipe.cookingSteps
    }

    private fun editRecipe() {
        val intent = Intent(this, AddRecipeActivity::class.java)
        intent.putExtra("recipe", currentRecipe)
        startActivity(intent)
        finish()
    }

    private fun deleteRecipe() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Удаление рецепта")
            .setMessage("Вы уверены, что хотите удалить рецепт \"${currentRecipe.title}\"?")
            .setPositiveButton("Удалить") { dialog, which ->
                // Здесь будет удаление из базы данных
                Toast.makeText(this, "Рецепт удален", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun shareRecipe() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, currentRecipe.title)
        shareIntent.putExtra(Intent.EXTRA_TEXT,
            "Рецепт: ${currentRecipe.title}\n\n" +
                    "Категория: ${currentRecipe.category}\n" +
                    "Описание: ${currentRecipe.description}\n\n" +
                    "Питательная ценность на порцию:\n" +
                    "• Калории: ${currentRecipe.caloriesPerServing} ккал\n" +
                    "• Белки: ${currentRecipe.proteinPerServing} г\n" +
                    "• Жиры: ${currentRecipe.fatPerServing} г\n" +
                    "• Углеводы: ${currentRecipe.carbsPerServing} г\n\n" +
                    "Шаги приготовления:\n${currentRecipe.cookingSteps}")
        startActivity(Intent.createChooser(shareIntent, "Поделиться рецептом"))
    }
}