package com.yourname.nutritiondiarysecond.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val productId: Int,
    val name: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val carbsPer100g: Double,
    val categoryId: Int,
    val isCustom: Boolean,
    val createdByUserId: Int,
    val barcode: String = "",
    var isSelected: Boolean = false
) : Parcelable {
    // Вычисляемое свойство для отображения
    val displayName: String
        get() = "$name (${caloriesPer100g} ккал/100г)"
}