package com.yourname.nutritiondiarysecond.models

import java.util.Date

data class DailyChallenge(
    val challengeId: Int,
    val title: String,
    val description: String,
    val category: String, // "Питание", "Спорт", "Здоровье"
    val isCompleted: Boolean,
    val dateAssigned: Date

) {
    // Вычисляемое свойство для иконки
    val icon: String
        get() = getIconByCategory(category)

    private fun getIconByCategory(category: String): String {
        return when (category) {
            "Питание" -> "🍎"
            "Спорт" -> "💪"
            "Здоровье" -> "❤️"
            else -> "🎯"
        }
    }
}