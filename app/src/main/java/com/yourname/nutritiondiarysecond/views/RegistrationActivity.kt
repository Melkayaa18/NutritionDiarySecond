package com.yourname.nutritiondiarysecond.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.database.NutritionRepository

class RegistrationActivity : AppCompatActivity() {

    private lateinit var usernameEntry: EditText
    private lateinit var passwordEntry: EditText
    private lateinit var registerButton: Button
    private lateinit var backButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        sharedPreferences = getSharedPreferences("NutritionDiary", Context.MODE_PRIVATE)
        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        usernameEntry = findViewById(R.id.usernameEntry)
        passwordEntry = findViewById(R.id.passwordEntry)
        registerButton = findViewById(R.id.registerButton)
        backButton = findViewById(R.id.backButton)
    }

    private fun setupClickListeners() {
        registerButton.setOnClickListener {
            val username = usernameEntry.text.toString().trim()
            val password = passwordEntry.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val repository = NutritionRepository(this)

            if (repository.checkUsernameExists(username)) {
                Toast.makeText(this, "Пользователь уже существует", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = repository.registerUser(username, password)

            if (userId > 0) {
                sharedPreferences.edit().apply {
                    putString("username", username)
                    putInt("userId", userId.toInt())
                    putBoolean("isLoggedIn", true)
                    apply()
                }

                Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}