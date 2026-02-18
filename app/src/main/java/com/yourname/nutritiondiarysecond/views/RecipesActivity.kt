package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.models.Recipe

class RecipesActivity : AppCompatActivity() {

    // Компаньон объект ДОЛЖЕН быть на уровне класса
    companion object {
        private const val FILTER_REQUEST = 1002
    }

    private lateinit var dailyRecipeName: TextView
    private lateinit var dailyRecipeDescription: TextView
    private lateinit var dailyRecipeButton: Button
    private lateinit var filterRecipesButton: Button
    private lateinit var myRecipesButton: Button
    private lateinit var addRecipeButton: Button
    private lateinit var randomRecipeButton: Button
    private lateinit var categoriesLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes)

        initializeViews()
        setupClickListeners()
        loadDailyRecipe()
        loadCategories()
    }

    private fun initializeViews() {
        dailyRecipeName = findViewById(R.id.dailyRecipeName)
        dailyRecipeDescription = findViewById(R.id.dailyRecipeDescription)
        dailyRecipeButton = findViewById(R.id.dailyRecipeButton)
        filterRecipesButton = findViewById(R.id.filterRecipesButton)
        myRecipesButton = findViewById(R.id.myRecipesButton)
        addRecipeButton = findViewById(R.id.addRecipeButton)
        randomRecipeButton = findViewById(R.id.randomRecipeButton)
        categoriesLayout = findViewById(R.id.categoriesLayout)
    }

    private fun setupClickListeners() {
        dailyRecipeButton.setOnClickListener {
            val mockRecipe = createMockRecipe()
            val intent = Intent(this, RecipeDetailsActivity::class.java)
            intent.putExtra("recipe", mockRecipe)
            startActivity(intent)
        }

        filterRecipesButton.setOnClickListener {
            val intent = Intent(this, RecipeFilterActivity::class.java)
            startActivityForResult(intent, FILTER_REQUEST)
        }

        myRecipesButton.setOnClickListener {
            val intent = Intent(this, MyRecipesActivity::class.java)
            startActivity(intent)
        }

        addRecipeButton.setOnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivity(intent)
        }
//аа
        randomRecipeButton.setOnClickListener {
            loadDailyRecipe()
            Toast.makeText(this, "🎲 Рецепт дня обновлен!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadDailyRecipe() {
        // Заглушка - случайный рецепт дня
        val recipes = listOf(
            "Смузи-боул с ягодами" to "Питательный смузи-боул для энергичного начала дня с свежими ягодами и орехами",
            "Куриный салат с авокадо" to "Свежий салат с куриной грудкой, авокадо и сезонными овощами",
            "Овсянка с ягодами" to "Классическая овсяная каша с ягодами и медом",
            "Рыба на пару с овощами" to "Легкое и полезное блюдо для ужина"
        )

        val randomRecipe = recipes.random()
        dailyRecipeName.text = randomRecipe.first
        dailyRecipeDescription.text = randomRecipe.second
    }

    private fun loadCategories() {
        val categories = listOf(
            "🍳 Завтрак" to "#FFB74D",
            "🍽️ Обед" to "#4DB6AC",
            "🌙 Ужин" to "#7986CB",
            "🍎 Перекус" to "#A1887F",
            "🍰 Десерт" to "#FF9E6D",
            "🥤 Напиток" to "#4DB6AC"
        )

        categoriesLayout.removeAllViews()

        categories.forEach { (name, color) ->
            val button = Button(this).apply {
                text = name
                setBackgroundColor(android.graphics.Color.parseColor(color))
                setTextColor(android.graphics.Color.WHITE)
                textSize = 14f
                setPadding(40, 20, 40, 20)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, 12)
                }
            }

            button.setOnClickListener {
                val categoryName = name.substring(2) // Убираем эмодзи
                val intent = Intent(this, CategoryRecipesActivity::class.java)
                intent.putExtra("category", categoryName)
                startActivity(intent)
            }

            categoriesLayout.addView(button)
        }
    }

    private fun createMockRecipe(): Recipe {
        return Recipe(
            recipeId = 1,
            title = dailyRecipeName.text.toString(),
            description = dailyRecipeDescription.text.toString(),
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

    // onActivityResult ДОЛЖЕН быть на уровне класса, после всех методов
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST && resultCode == RESULT_OK) {
            val category = data?.getStringExtra("category")
            val caloriesFrom = data?.getIntExtra("caloriesFrom", 0)
            val caloriesTo = data?.getIntExtra("caloriesTo", 1000)

            // Здесь будет применение фильтров к рецептам
            Toast.makeText(this, "Фильтры применены: $category", Toast.LENGTH_SHORT).show()
        }
    }
}