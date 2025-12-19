package com.yourname.nutritiondiarysecond.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Achievements")
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true)
    val achievementId: Int = 0,
    val name: String,
    val description: String?
)