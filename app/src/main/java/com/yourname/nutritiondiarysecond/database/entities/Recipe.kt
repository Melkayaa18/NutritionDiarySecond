package com.yourname.nutritiondiarysecond.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeId: Int = 0,
    val title: String,
    val description: String?,
    val category: String,
    val caloriesPerServing: Double,
    val proteinPerServing: Double,
    val fatPerServing: Double,
    val carbsPerServing: Double,
    val imagePath: String?,
    val cookingSteps: String?,
    val isActive: Boolean = true
) {
    fun toRecipe(): com.yourname.nutritiondiarysecond.models.Recipe {
        return com.yourname.nutritiondiarysecond.models.Recipe(
            recipeId = recipeId,
            title = title,
            description = description ?: "",
            category = category,
            caloriesPerServing = caloriesPerServing,
            proteinPerServing = proteinPerServing,
            fatPerServing = fatPerServing,
            carbsPerServing = carbsPerServing,
            imagePath = imagePath,
            cookingSteps = cookingSteps ?: "",
            isActive = isActive
        )
    }
}