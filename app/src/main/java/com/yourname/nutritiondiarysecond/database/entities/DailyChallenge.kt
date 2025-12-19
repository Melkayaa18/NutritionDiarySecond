package com.yourname.nutritiondiarysecond.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "DailyChallenges",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DailyChallengeEntity(
    @PrimaryKey(autoGenerate = true)
    val challengeId: Int = 0,
    val userId: Int,
    val title: String,
    val description: String?,
    val category: String,
    val isCompleted: Boolean = false,
    val dateAssigned: Long = Date().time
) {
    fun toDailyChallenge(): com.yourname.nutritiondiarysecond.models.DailyChallenge {
        return com.yourname.nutritiondiarysecond.models.DailyChallenge(
            challengeId = challengeId,
            title = title,
            description = description ?: "",
            category = category,
            isCompleted = isCompleted,
            dateAssigned = Date(dateAssigned)
        )
    }
}