package com.yourname.nutritiondiarysecond.models

import java.util.Date

data class User(
    val userId: Int,
    val username: String,
    val password: String,
    val email: String,
    val age: Int,
    val height: Double,
    val currentWeight: Double,
    val desiredWeight: Double,
    val dailyCalorieGoal: Int,
    val registrationDate: Date
)