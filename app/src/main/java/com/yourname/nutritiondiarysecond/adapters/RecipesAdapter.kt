package com.yourname.nutritiondiarysecond.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.models.Recipe

class RecipesAdapter(
    private var recipes: List<Recipe>,
    private val onRecipeClick: (Recipe) -> Unit,
    private val onRecipeLongClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.recipeTitle)
        val categoryTextView: TextView = itemView.findViewById(R.id.recipeCategory)
        val caloriesTextView: TextView = itemView.findViewById(R.id.recipeCalories)
        val nutritionTextView: TextView = itemView.findViewById(R.id.recipeNutrition)
        val cardView: com.google.android.material.card.MaterialCardView = itemView.findViewById(R.id.recipeCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]

        holder.titleTextView.text = recipe.title
        holder.categoryTextView.text = recipe.category
        holder.caloriesTextView.text = "${recipe.caloriesPerServing} ккал/порция"
        holder.nutritionTextView.text = "Б: ${recipe.proteinPerServing}г, Ж: ${recipe.fatPerServing}г, У: ${recipe.carbsPerServing}г"

        // Устанавливаем цвет категории
        val categoryColor = getCategoryColor(recipe.category, holder.itemView.context)
        holder.cardView.setCardBackgroundColor(categoryColor)

        holder.itemView.setOnClickListener {
            onRecipeClick(recipe)
        }

        holder.itemView.setOnLongClickListener {
            onRecipeLongClick(recipe)
            true
        }
    }

    override fun getItemCount(): Int = recipes.size

    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    private fun getCategoryColor(category: String, context: android.content.Context): Int {
        return when (category) {
            "Завтрак" -> ContextCompat.getColor(context, R.color.primary_light)
            "Обед" -> ContextCompat.getColor(context, R.color.secondary_color)
            "Ужин" -> ContextCompat.getColor(context, R.color.accent_color)
            "Перекус" -> ContextCompat.getColor(context, android.R.color.holo_orange_light)
            "Десерт" -> ContextCompat.getColor(context, android.R.color.holo_purple)
            "Напиток" -> ContextCompat.getColor(context, android.R.color.holo_blue_light)
            else -> ContextCompat.getColor(context, R.color.background_white)
        }
    }
}