package com.yourname.nutritiondiarysecond.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "UserAchievements",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AchievementEntity::class,
            parentColumns = ["achievementId"],
            childColumns = ["achievementId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserAchievementEntity(
    @PrimaryKey(autoGenerate = true)
    val userAchievementId: Int = 0,
    val userId: Int,
    val achievementId: Int,
    val achievedDate: Long = Date().time
)