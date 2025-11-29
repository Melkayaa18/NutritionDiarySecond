package com.yourname.nutritiondiarysecond.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val recipeId: Int,
    val title: String,
    val description: String,
    val category: String,
    val caloriesPerServing: Double,
    val proteinPerServing: Double,
    val fatPerServing: Double,
    val carbsPerServing: Double,
    val imagePath: String?,
    val cookingSteps: String,
    val isActive: Boolean
) : Parcelable