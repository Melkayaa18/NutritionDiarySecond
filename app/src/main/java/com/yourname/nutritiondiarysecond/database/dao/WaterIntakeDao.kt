package com.yourname.nutritiondiarysecond.database.dao

import androidx.room.*
import com.yourname.nutritiondiarysecond.database.entities.WaterIntakeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterIntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaterIntake(waterIntake: WaterIntakeEntity): Long

    @Update
    suspend fun updateWaterIntake(waterIntake: WaterIntakeEntity)

    @Delete
    suspend fun deleteWaterIntake(waterIntake: WaterIntakeEntity)

    @Query("SELECT * FROM WaterIntake WHERE userId = :userId AND intakeDate >= :startDate AND intakeDate < :endDate")
    fun getWaterIntakeByDate(userId: Int, startDate: Long, endDate: Long): Flow<List<WaterIntakeEntity>>

    @Query("SELECT SUM(amount) FROM WaterIntake WHERE userId = :userId AND intakeDate >= :startDate AND intakeDate < :endDate")
    suspend fun getTotalWaterIntake(userId: Int, startDate: Long, endDate: Long): Double?

    @Query("DELETE FROM WaterIntake WHERE waterId = :waterId")
    suspend fun deleteWaterIntakeById(waterId: Int)

    @Query("DELETE FROM WaterIntake WHERE userId = :userId AND intakeDate >= :startDate AND intakeDate < :endDate")
    suspend fun deleteWaterIntakeByDate(userId: Int, startDate: Long, endDate: Long)
}