package com.yourname.nutritiondiarysecond.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.database.NutritionRepository

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEntry: EditText
    private lateinit var passwordEntry: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("NutritionDiary", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        usernameEntry = findViewById(R.id.usernameEntry)
        passwordEntry = findViewById(R.id.passwordEntry)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)
    }

    private fun setupClickListeners() {
        loginButton.setOnClickListener {
            val username = usernameEntry.text.toString().trim()
            val password = passwordEntry.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val repository = NutritionRepository(this)
            val success = repository.loginUser(username, password)

            if (success) {
                val user = repository.getUserByUsername(username) // Нужно добавить этот метод!
                if (user != null) {
                    sharedPreferences.edit().apply {
                        putString("username", username)
                        putInt("userId", user.id)
                        putBoolean("isLoggedIn", true)
                        apply()
                    }
                }

                Toast.makeText(this, "Вход выполнен!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

    // ДОБАВЬТЕ этот метод в NutritionRepository:
    // fun getUserByUsername(username: String): UserEntity? {
    //     return database.userDao().getUserByUsername(username)
    // }
}