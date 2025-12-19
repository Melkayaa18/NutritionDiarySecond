package com.yourname.nutritiondiarysecond.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val username: String,
    val password: String,
    val email: String?,
    val age: Int,
    val height: Double,
    val currentWeight: Double,
    val desiredWeight: Double,
    val dailyCalorieGoal: Int,
    val registrationDate: Long = Date().time // Сохраняем как Long
) {
    // Конвертируем Long обратно в Date
    val registrationDateAsDate: Date
        get() = Date(registrationDate)
}