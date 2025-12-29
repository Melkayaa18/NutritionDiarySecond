package com.yourname.nutritiondiarysecond.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yourname.nutritiondiarysecond.database.dao.UserDao
import com.yourname.nutritiondiarysecond.database.entities.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nutrition.db"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // Временно для простоты
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}