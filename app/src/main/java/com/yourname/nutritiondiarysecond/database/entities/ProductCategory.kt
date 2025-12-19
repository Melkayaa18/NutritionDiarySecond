package com.yourname.nutritiondiarysecond.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProductCategories")
data class ProductCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Int = 0,
    val name: String
)