package com.yourname.nutritiondiarysecond.database.dao

import androidx.room.*
import com.yourname.nutritiondiarysecond.database.entities.DiaryEntryEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface DiaryEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: DiaryEntryEntity): Long

    @Update
    suspend fun updateEntry(entry: DiaryEntryEntity)

    @Delete
    suspend fun deleteEntry(entry: DiaryEntryEntity)

    @Query("SELECT * FROM DiaryEntries WHERE entryId = :entryId")
    suspend fun getEntryById(entryId: Int): DiaryEntryEntity?

    @Query("SELECT * FROM DiaryEntries WHERE userId = :userId AND entryDate >= :startDate AND entryDate < :endDate")
    fun getEntriesByDate(userId: Int, startDate: Long, endDate: Long): Flow<List<DiaryEntryEntity>>

    @Query("SELECT * FROM DiaryEntries WHERE userId = :userId AND mealTypeId = :mealTypeId AND entryDate >= :startDate AND entryDate < :endDate")
    fun getEntriesByMealType(userId: Int, mealTypeId: Int, startDate: Long, endDate: Long): Flow<List<DiaryEntryEntity>>

    @Query("""
        SELECT SUM(calories) as totalCalories, 
               SUM(protein) as totalProtein, 
               SUM(fat) as totalFat, 
               SUM(carbs) as totalCarbs 
        FROM DiaryEntries 
        WHERE userId = :userId AND entryDate >= :startDate AND entryDate < :endDate
    """)
    suspend fun getDailySummary(userId: Int, startDate: Long, endDate: Long): DailySummary?

    @Query("DELETE FROM DiaryEntries WHERE entryId = :entryId")
    suspend fun deleteEntryById(entryId: Int)

    @Query("DELETE FROM DiaryEntries WHERE userId = :userId AND entryDate >= :startDate AND entryDate < :endDate")
    suspend fun deleteEntriesByDate(userId: Int, startDate: Long, endDate: Long)

    data class DailySummary(
        val totalCalories: Double?,
        val totalProtein: Double?,
        val totalFat: Double?,
        val totalCarbs: Double?
    )
}