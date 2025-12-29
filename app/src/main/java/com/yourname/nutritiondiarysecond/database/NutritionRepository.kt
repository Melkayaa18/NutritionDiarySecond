package com.yourname.nutritiondiarysecond.database

import android.content.Context
import com.yourname.nutritiondiarysecond.database.entities.UserEntity

class NutritionRepository(private val context: Context) {

    private val database = AppDatabase.getInstance(context)

    fun registerUser(username: String, password: String): Long {
        val user = UserEntity(username = username, password = password)
        return database.userDao().insert(user)
    }

    fun loginUser(username: String, password: String): Boolean {
        val user = database.userDao().getUserByUsername(username)
        return user != null && user.password == password
    }

    fun checkUsernameExists(username: String): Boolean {
        val count = database.userDao().checkUsernameExists(username)
        return count > 0
    }
    fun getUserByUsername(username: String): UserEntity? {
        return database.userDao().getUserByUsername(username)
    }
}