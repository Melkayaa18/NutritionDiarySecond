package com.yourname.nutritiondiarysecond.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "Products",
    foreignKeys = [
        ForeignKey(
            entity = ProductCategoryEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["createdByUserId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val productId: Int = 0,
    val name: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val carbsPer100g: Double,
    val categoryId: Int?,
    val isCustom: Boolean,
    val createdByUserId: Int?,
    val barcode: String? = ""
) {
    // Функция для конвертации в модель
    fun toProduct(): com.yourname.nutritiondiarysecond.models.Product {
        return com.yourname.nutritiondiarysecond.models.Product(
            productId = productId,
            name = name,
            caloriesPer100g = caloriesPer100g,
            proteinPer100g = proteinPer100g,
            fatPer100g = fatPer100g,
            carbsPer100g = carbsPer100g,
            categoryId = categoryId ?: 0,
            isCustom = isCustom,
            createdByUserId = createdByUserId ?: 0,
            barcode = barcode ?: ""
        )
    }
}