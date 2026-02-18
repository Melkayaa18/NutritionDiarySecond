package com.yourname.nutritiondiarysecond.models

data class DailySummaryResponse(
    val date: String,
    val totalCalories: Double,
    val totalProtein: Double,
    val totalFat: Double,
    val totalCarbs: Double,
    val challenges: List<DailyChallenge> = emptyList()
)