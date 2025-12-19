package com.yourname.nutritiondiarysecond.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class WaterIntake(
    val waterId: Int,
    val userId: Int,
    val intakeDate: Date,
    val amount: Double,
    val createdAt: Date?
) : Parcelable