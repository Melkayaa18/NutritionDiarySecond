package com.yourname.nutritiondiarysecond.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MealTypes")
data class MealTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val mealTypeId: Int = 0,
    val name: String
)