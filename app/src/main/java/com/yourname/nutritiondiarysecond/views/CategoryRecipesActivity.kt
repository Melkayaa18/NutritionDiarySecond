package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.adapters.RecipesAdapter
import com.yourname.nutritiondiarysecond.models.Recipe

class CategoryRecipesActivity : AppCompatActivity() {

    private lateinit var categoryTitle: TextView
    private lateinit var recipesCount: TextView
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var emptyState: LinearLayout

    private lateinit var recipesAdapter: RecipesAdapter
    private val recipes = mutableListOf<Recipe>()
    private var currentCategory = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_recipes)

        // Получаем категорию из Intent
        currentCategory = intent.getStringExtra("category") ?: "Рецепты"

        initializeViews()
        setupRecyclerView()
        loadCategoryRecipes()
    }

    private fun initializeViews() {
        categoryTitle = findViewById(R.id.categoryTitle)
        recipesCount = findViewById(R.id.recipesCount)
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView)
        emptyState = findViewById(R.id.emptyState)

        categoryTitle.text = currentCategory
    }

    private fun setupRecyclerView() {
        recipesAdapter = RecipesAdapter(
            recipes,
            onRecipeClick = { recipe ->
                openRecipeDetails(recipe)
            },
            onRecipeLongClick = { recipe ->
                // Для категорий долгое нажатие не нужно
            }
        )

        recipesRecyclerView.layoutManager = LinearLayoutManager(this)
        recipesRecyclerView.adapter = recipesAdapter
    }

    private fun loadCategoryRecipes() {
        // Заглушка - временные данные
        recipes.clear()
        recipes.addAll(createMockRecipesForCategory(currentCategory))
        recipesAdapter.updateRecipes(recipes)

        updateRecipesDisplay()
    }

    private fun createMockRecipesForCategory(category: String): List<Recipe> {
        return when (category) {
            "Завтрак" -> listOf(
                Recipe(
                    recipeId = 1,
                    title = "Овсянка с ягодами",
                    description = "Полезный завтрак с овсяными хлопьями и свежими ягодами",
                    category = "Завтрак",
                    caloriesPerServing = 250.0,
                    proteinPerServing = 8.0,
                    fatPerServing = 5.0,
                    carbsPerServing = 45.0,
                    imagePath = null,
                    cookingSteps = "1. Сварить овсянку\n2. Добавить ягоды\n3. Подавать теплым",
                    isActive = true
                ),
                Recipe(
                    recipeId = 2,
                    title = "Яичница с овощами",
                    description = "Быстрый и питательный завтрак",
                    category = "Завтрак",
                    caloriesPerServing = 300.0,
                    proteinPerServing = 20.0,
                    fatPerServing = 15.0,
                    carbsPerServing = 10.0,
                    imagePath = null,
                    cookingSteps = "1. Обжарить овощи\n2. Добавить яйца\n3. Готовить 5 минут",
                    isActive = true
                )
            )
            "Обед" -> listOf(
                Recipe(
                    recipeId = 3,
                    title = "Куриный салат",
                    description = "Легкий салат с куриной грудкой и овощами",
                    category = "Обед",
                    caloriesPerServing = 180.0,
                    proteinPerServing = 25.0,
                    fatPerServing = 6.0,
                    carbsPerServing = 8.0,
                    imagePath = null,
                    cookingSteps = "1. Нарезать овощи\n2. Добавить курицу\n3. Заправить",
                    isActive = true
                )
            )
            else -> emptyList()
        }
    }

    private fun updateRecipesDisplay() {
        recipesCount.text = "${recipes.size} рецептов"

        // Показываем/скрываем пустое состояние
        if (recipes.isEmpty()) {
            emptyState.visibility = android.view.View.VISIBLE
            recipesRecyclerView.visibility = android.view.View.GONE
        } else {
            emptyState.visibility = android.view.View.GONE
            recipesRecyclerView.visibility = android.view.View.VISIBLE
        }
    }

    private fun openRecipeDetails(recipe: Recipe) {
        val intent = Intent(this, RecipeDetailsActivity::class.java)
        intent.putExtra("recipe", recipe)
        startActivity(intent)
    }
}