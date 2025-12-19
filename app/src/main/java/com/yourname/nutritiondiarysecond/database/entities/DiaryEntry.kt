package com.yourname.nutritiondiarysecond.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "DiaryEntries",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MealTypeEntity::class,
            parentColumns = ["mealTypeId"],
            childColumns = ["mealTypeId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DiaryEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val entryId: Int = 0,
    val userId: Int,
    val mealTypeId: Int,
    val productId: Int,
    val quantity: Double,
    val entryDate: Long = Date().time,
    val calories: Double,
    val protein: Double,
    val fat: Double,
    val carbs: Double
)