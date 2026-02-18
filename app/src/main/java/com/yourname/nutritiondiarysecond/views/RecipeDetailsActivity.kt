package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.models.Recipe

class RecipeDetailsActivity : AppCompatActivity() {

    private lateinit var recipeTitle: TextView
    private lateinit var recipeCategory: TextView
    private lateinit var caloriesValue: TextView
    private lateinit var proteinValue: TextView
    private lateinit var fatValue: TextView
    private lateinit var carbsValue: TextView
    private lateinit var descriptionValue: TextView
    private lateinit var cookingStepsValue: TextView
    private lateinit var addToDiaryButton: Button
    private lateinit var editButton: Button

    private lateinit var currentRecipe: Recipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        // Получаем рецепт из Intent
        currentRecipe = intent.getParcelableExtra("recipe") ?: createMockRecipe()

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
        addToDiaryButton = findViewById(R.id.addToDiaryButton)
        editButton = findViewById(R.id.editButton)
    }

    private fun setupClickListeners() {
        addToDiaryButton.setOnClickListener {
            addRecipeToDiary()
        }

        editButton.setOnClickListener {
            editRecipe()
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

    private fun createMockRecipe(): Recipe {
        return Recipe(
            recipeId = 1,
            title = "Смузи-боул с ягодами",
            description = "Питательный смузи-боул для энергичного начала дня с свежими ягодами и орехами",
            category = "Завтрак",
            caloriesPerServing = 350.0,
            proteinPerServing = 15.0,
            fatPerServing = 10.0,
            carbsPerServing = 45.0,
            imagePath = null,
            cookingSteps = "1. Подготовьте все ингредиенты\n2. Смешайте в блендере\n3. Украсьте ягодами и орехами",
            isActive = true
        )
    }

    private fun addRecipeToDiary() {
        // Заглушка - добавление рецепта в дневник
        android.widget.Toast.makeText(this, "Рецепт добавлен в дневник!", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun editRecipe() {
        // Заглушка - переход к редактированию рецепта
        android.widget.Toast.makeText(this, "Редактирование рецепта", android.widget.Toast.LENGTH_SHORT).show()

        val intent = Intent(this, AddRecipeActivity::class.java)
        intent.putExtra("recipe", currentRecipe)
        startActivity(intent)
    }
}