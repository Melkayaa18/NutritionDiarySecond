package com.yourname.nutritiondiarysecond.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yourname.nutritiondiarysecond.database.entities.UserEntity

@Dao
interface UserDao {

    @Insert
    fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    fun checkUsernameExists(username: String): Int

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Int): UserEntity?
}