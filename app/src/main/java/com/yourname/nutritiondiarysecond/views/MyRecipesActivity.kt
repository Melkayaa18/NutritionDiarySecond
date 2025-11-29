package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.adapters.RecipesAdapter
import com.yourname.nutritiondiarysecond.models.Recipe

class MyRecipesActivity : AppCompatActivity() {

    private lateinit var recipesCount: TextView
    private lateinit var searchEntry: EditText
    private lateinit var filterButton: Button
    private lateinit var sortButton: Button
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var emptyState: LinearLayout
    private lateinit var createFirstRecipeButton: Button

    private lateinit var recipesAdapter: RecipesAdapter
    private val allRecipes = mutableListOf<Recipe>()
    private val filteredRecipes = mutableListOf<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_recipes)

        initializeViews()
        setupRecyclerView()
        setupClickListeners()
        loadMyRecipes()
    }

    private fun initializeViews() {
        recipesCount = findViewById(R.id.recipesCount)
        searchEntry = findViewById(R.id.searchEntry)
        filterButton = findViewById(R.id.filterButton)
        sortButton = findViewById(R.id.sortButton)
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView)
        emptyState = findViewById(R.id.emptyState)
        createFirstRecipeButton = findViewById(R.id.createFirstRecipeButton)

        // Настройка поиска
        searchEntry.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterRecipes(s.toString())
            }
        })
    }

    private fun setupRecyclerView() {
        recipesAdapter = RecipesAdapter(
            filteredRecipes,
            onRecipeClick = { recipe ->
                openRecipeDetails(recipe)
            },
            onRecipeLongClick = { recipe ->
                showRecipeOptions(recipe)
            }
        )

        recipesRecyclerView.layoutManager = LinearLayoutManager(this)
        recipesRecyclerView.adapter = recipesAdapter
    }

    private fun setupClickListeners() {
        filterButton.setOnClickListener {
            showFilterDialog()
        }

        sortButton.setOnClickListener {
            showSortDialog()
        }

        createFirstRecipeButton.setOnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadMyRecipes() {
        // Заглушка - временные данные
        allRecipes.clear()
        allRecipes.addAll(createMockRecipes())
        filteredRecipes.clear()
        filteredRecipes.addAll(allRecipes)

        updateRecipesDisplay()
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
            )
        )
    }

    private fun filterRecipes(searchText: String) {
        filteredRecipes.clear()
        if (searchText.isEmpty()) {
            filteredRecipes.addAll(allRecipes)
        } else {
            val query = searchText.toLowerCase()
            allRecipes.forEach { recipe ->
                if (recipe.title.toLowerCase().contains(query) ||
                    recipe.description.toLowerCase().contains(query) ||
                    recipe.category.toLowerCase().contains(query)) {
                    filteredRecipes.add(recipe)
                }
            }
        }
        updateRecipesDisplay()
    }

    private fun updateRecipesDisplay() {
        recipesAdapter.updateRecipes(filteredRecipes)
        recipesCount.text = "${filteredRecipes.size} рецептов"

        // Показываем/скрываем пустое состояние
        if (filteredRecipes.isEmpty()) {
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

    private fun showRecipeOptions(recipe: Recipe) {
        val options = arrayOf("Редактировать", "Удалить", "Поделиться", "Отмена")

        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(recipe.title)
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> editRecipe(recipe)
                1 -> deleteRecipe(recipe)
                2 -> shareRecipe(recipe)
            }
        }
        builder.show()
    }

    private fun editRecipe(recipe: Recipe) {
        val intent = Intent(this, AddRecipeActivity::class.java)
        intent.putExtra("recipe", recipe)
        startActivity(intent)
    }

    private fun deleteRecipe(recipe: Recipe) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Удаление рецепта")
        builder.setMessage("Вы уверены, что хотите удалить рецепт \"${recipe.title}\"?")
        builder.setPositiveButton("Удалить") { dialog, which ->
            allRecipes.remove(recipe)
            filteredRecipes.remove(recipe)
            updateRecipesDisplay()
            Toast.makeText(this, "Рецепт удален", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Отмена", null)
        builder.show()
    }

    private fun shareRecipe(recipe: Recipe) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, recipe.title)
        shareIntent.putExtra(Intent.EXTRA_TEXT,
            "${recipe.title}\n\n${recipe.description}\n\nКалории: ${recipe.caloriesPerServing} ккал/порция")
        startActivity(Intent.createChooser(shareIntent, "Поделиться рецептом"))
    }

    private fun showFilterDialog() {
        Toast.makeText(this, "Фильтры будут доступны позже", Toast.LENGTH_SHORT).show()
    }

    private fun showSortDialog() {
        val sortOptions = arrayOf("По названию (А-Я)", "По названию (Я-А)", "По калориям (возр.)", "По калориям (убыв.)")

        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Сортировка рецептов")
        builder.setItems(sortOptions) { dialog, which ->
            when (which) {
                0 -> sortRecipesByName(true)
                1 -> sortRecipesByName(false)
                2 -> sortRecipesByCalories(true)
                3 -> sortRecipesByCalories(false)
            }
        }
        builder.show()
    }

    private fun sortRecipesByName(ascending: Boolean) {
        if (ascending) {
            allRecipes.sortBy { it.title }
        } else {
            allRecipes.sortByDescending { it.title }
        }
        filterRecipes(searchEntry.text.toString())
    }

    private fun sortRecipesByCalories(ascending: Boolean) {
        if (ascending) {
            allRecipes.sortBy { it.caloriesPerServing }
        } else {
            allRecipes.sortByDescending { it.caloriesPerServing }
        }
        filterRecipes(searchEntry.text.toString())
    }
}