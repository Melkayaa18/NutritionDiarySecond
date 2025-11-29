package com.yourname.nutritiondiarysecond.models

import java.util.Date

data class DiaryEntry(
    val entryId: Int,
    val userId: Int,
    val mealTypeId: Int,
    val productId: Int,
    val quantity: Double,
    val entryDate: Date,
    val calories: Double,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val productName: String,
    val mealTypeName: String
)