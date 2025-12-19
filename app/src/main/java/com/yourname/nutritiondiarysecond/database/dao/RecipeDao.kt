package com.yourname.nutritiondiarysecond.database.dao

import androidx.room.*
import com.yourname.nutritiondiarysecond.database.entities.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM Recipes WHERE recipeId = :recipeId")
    suspend fun getRecipeById(recipeId: Int): RecipeEntity?

    @Query("SELECT * FROM Recipes WHERE category = :category")
    fun getRecipesByCategory(category: String): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM Recipes WHERE title LIKE '%' || :search || '%'")
    fun searchRecipes(search: String): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM Recipes WHERE isActive = 1")
    fun getAllActiveRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM Recipes ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomRecipe(): RecipeEntity?

    @Query("DELETE FROM Recipes WHERE recipeId = :recipeId")
    suspend fun deleteRecipeById(recipeId: Int)
}