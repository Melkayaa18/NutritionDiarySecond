package com.yourname.nutritiondiarysecond.database.dao

import androidx.room.*
import com.yourname.nutritiondiarysecond.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM Users WHERE userId = :userId")
    suspend fun getUserById(userId: Int): UserEntity?

    @Query("SELECT * FROM Users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM Users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT COUNT(*) FROM Users WHERE username = :username")
    suspend fun checkUsernameExists(username: String): Int

    @Query("SELECT * FROM Users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("DELETE FROM Users")
    suspend fun deleteAllUsers()
}