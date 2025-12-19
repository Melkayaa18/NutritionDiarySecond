package com.yourname.nutritiondiarysecond.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "WaterIntake",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WaterIntakeEntity(
    @PrimaryKey(autoGenerate = true)
    val waterId: Int = 0,
    val userId: Int,
    val intakeDate: Long = Date().time,
    val amount: Double, // в мл
    val createdAt: Long? = Date().time
)