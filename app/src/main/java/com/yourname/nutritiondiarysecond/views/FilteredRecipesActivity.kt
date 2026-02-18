package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.adapters.RecipesAdapter
import com.yourname.nutritiondiarysecond.models.Recipe

class FilteredRecipesActivity : AppCompatActivity() {

    private lateinit var filterTitle: TextView
    private lateinit var filterDescription: TextView
    private lateinit var activeFiltersCard: com.google.android.material.card.MaterialCardView
    private lateinit var activeFiltersLayout: LinearLayout
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var emptyState: LinearLayout
    private lateinit var changeFiltersButton: Button

    private lateinit var recipesAdapter: RecipesAdapter
    private val allRecipes = mutableListOf<Recipe>()
    private val filteredRecipes = mutableListOf<Recipe>()

    // Параметры фильтрации
    private var category: String? = null
    private var caloriesFrom: Int? = null
    private var caloriesTo: Int? = null
    private var maxTime: Int? = null
    private var difficulty: String? = null
    private var ingredients: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtered_recipes)

        // Получаем параметры фильтрации из Intent
        getFilterParameters()

        initializeViews()
        setupRecyclerView()
        setupClickListeners()
        loadAllRecipes()
        applyFilters()
        displayActiveFilters()
    }

    private fun getFilterParameters() {
        category = intent.getStringExtra("category")
        caloriesFrom = intent.getIntExtra("caloriesFrom", 0).takeIf { it > 0 }
        caloriesTo = intent.getIntExtra("caloriesTo", 1000).takeIf { it < 1000 }
        maxTime = intent.getIntExtra("maxTime", 0).takeIf { it > 0 }
        difficulty = intent.getStringExtra("difficulty")
        ingredients = intent.getStringExtra("ingredients")
    }

    private fun initializeViews() {
        filterTitle = findViewById(R.id.filterTitle)
        filterDescription = findViewById(R.id.filterDescription)
        activeFiltersCard = findViewById(R.id.activeFiltersCard)
        activeFiltersLayout = findViewById(R.id.activeFiltersLayout)
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView)
        emptyState = findViewById(R.id.emptyState)
        changeFiltersButton = findViewById(R.id.changeFiltersButton)
    }

    private fun setupRecyclerView() {
        recipesAdapter = RecipesAdapter(
            filteredRecipes,
            onRecipeClick = { recipe ->
                openRecipeDetails(recipe)
            },
            onRecipeLongClick = { recipe ->
                // Для фильтрованных рецептов долгое нажатие не нужно
            }
        )

        recipesRecyclerView.layoutManager = LinearLayoutManager(this)
        recipesRecyclerView.adapter = recipesAdapter
    }

    private fun setupClickListeners() {
        changeFiltersButton.setOnClickListener {
            val intent = Intent(this, RecipeFilterActivity::class.java)
            // Можно передать текущие параметры фильтрации
            startActivity(intent)
            finish()
        }
    }

    private fun loadAllRecipes() {
        // Заглушка - загружаем все рецепты
        allRecipes.clear()
        allRecipes.addAll(createMockRecipes())
    }

    private fun createMockRecipes(): List<Recipe> {
        return listOf(
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
            ),
            Recipe(
                recipeId = 3,
                title = "Рыба на пару с овощами",
                description = "Легкое и полезное блюдо для ужина",
                category = "Ужин",
                caloriesPerServing = 150.0,
                proteinPerServing = 22.0,
                fatPerServing = 3.0,
                carbsPerServing = 10.0,
                imagePath = null,
                cookingSteps = "1. Приготовить на пару\n2. Подавать с овощами",
                isActive = true
            ),
            Recipe(
                recipeId = 4,
                title = "Протеиновый коктейль",
                description = "Быстрый и питательный перекус",
                category = "Перекус",
                caloriesPerServing = 200.0,
                proteinPerServing = 30.0,
                fatPerServing = 2.0,
                carbsPerServing = 15.0,
                imagePath = null,
                cookingSteps = "1. Смешать все ингредиенты в блендере",
                isActive = true
            )
        )
    }

    private fun applyFilters() {
        filteredRecipes.clear()

        if (hasActiveFilters()) {
            // Применяем фильтры
            allRecipes.forEach { recipe ->
                if (matchesFilters(recipe)) {
                    filteredRecipes.add(recipe)
                }
            }
        } else {
            // Если нет фильтров, показываем все рецепты
            filteredRecipes.addAll(allRecipes)
        }

        updateRecipesDisplay()
    }

    private fun hasActiveFilters(): Boolean {
        return category != null || caloriesFrom != null || caloriesTo != null ||
                maxTime != null || difficulty != null || !ingredients.isNullOrEmpty()
    }

    private fun matchesFilters(recipe: Recipe): Boolean {
        // Фильтр по категории
        if (category != null && recipe.category != category) {
            return false
        }

        // Фильтр по калориям
        if (caloriesFrom != null && recipe.caloriesPerServing < caloriesFrom!!) {
            return false
        }
        if (caloriesTo != null && recipe.caloriesPerServing > caloriesTo!!) {
            return false
        }

        // Фильтр по времени (заглушка - предполагаем, что у всех рецептов время приготовления 30 минут)
        if (maxTime != null && maxTime!! < 30) {
            return false
        }

        // Фильтр по сложности (заглушка)
        if (difficulty != null) {
            // В реальном приложении здесь будет проверка сложности рецепта
        }

        // Фильтр по ингредиентам
        if (!ingredients.isNullOrEmpty()) {
            val searchIngredients = ingredients!!.toLowerCase()
            if (!recipe.title.toLowerCase().contains(searchIngredients) &&
                !recipe.description.toLowerCase().contains(searchIngredients)) {
                return false
            }
        }

        return true
    }

    private fun updateRecipesDisplay() {
        recipesAdapter.updateRecipes(filteredRecipes)
        filterDescription.text = "Найдено рецептов: ${filteredRecipes.size}"

        // Показываем/скрываем пустое состояние
        if (filteredRecipes.isEmpty()) {
            emptyState.visibility = android.view.View.VISIBLE
            recipesRecyclerView.visibility = android.view.View.GONE
        } else {
            emptyState.visibility = android.view.View.GONE
            recipesRecyclerView.visibility = android.view.View.VISIBLE
        }
    }

    private fun displayActiveFilters() {
        activeFiltersLayout.removeAllViews()

        val activeFilters = mutableListOf<String>()

        category?.let { activeFilters.add("Категория: $it") }
        caloriesFrom?.let { activeFilters.add("Калории от: $it") }
        caloriesTo?.let { activeFilters.add("Калории до: $it") }
        maxTime?.let { activeFilters.add("Время до: $it мин") }
        difficulty?.let { activeFilters.add("Сложность: $it") }
        ingredients?.let { activeFilters.add("Ингредиенты: $it") }

        if (activeFilters.isNotEmpty()) {
            activeFiltersCard.visibility = android.view.View.VISIBLE

            activeFilters.forEach { filterText ->
                val chip = LayoutInflater.from(this).inflate(
                    R.layout.chip_filter,
                    activeFiltersLayout,
                    false
                ) as Chip

                chip.text = filterText
                chip.setOnCloseIconClickListener {
                    removeFilter(filterText)
                }

                activeFiltersLayout.addView(chip)
            }
        } else {
            activeFiltersCard.visibility = android.view.View.GONE
        }
    }

    private fun removeFilter(filterText: String) {
        // Упрощенная логика удаления фильтра
        when {
            filterText.startsWith("Категория:") -> category = null
            filterText.startsWith("Калории от:") -> caloriesFrom = null
            filterText.startsWith("Калории до:") -> caloriesTo = null
            filterText.startsWith("Время до:") -> maxTime = null
            filterText.startsWith("Сложность:") -> difficulty = null
            filterText.startsWith("Ингредиенты:") -> ingredients = null
        }

        applyFilters()
        displayActiveFilters()
    }

    private fun openRecipeDetails(recipe: Recipe) {
        val intent = Intent(this, RecipeDetailsActivity::class.java)
        intent.putExtra("recipe", recipe)
        startActivity(intent)
    }
}