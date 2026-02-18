package com.yourname.nutritiondiarysecond.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import com.yourname.nutritiondiarysecond.R
//import com.yourname.nutritiondiarysecond.database.NutritionRepository

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEntry: EditText
    private lateinit var passwordEntry: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var skipButton: Button
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("NutritionDiary", MODE_PRIVATE)
        if (isUserLoggedIn() && getCurrentUsername() != "Гость") {
            startMainActivity()
            return
        }

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        usernameEntry = findViewById(R.id.usernameEntry)
        passwordEntry = findViewById(R.id.passwordEntry)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)
        skipButton = findViewById(R.id.skipButton)
        loadingIndicator = findViewById(R.id.loadingIndicator)
    }

    private fun setupClickListeners() {
        loginButton.setOnClickListener { attemptLogin() }
        registerButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        skipButton.setOnClickListener { loginAsGuest() }
    }
    //аа
    private fun attemptLogin() {
        val username = usernameEntry.text.toString().trim()
        val password = passwordEntry.text.toString()
        if (username.isEmpty() || password.isEmpty()) {
            showError("Пожалуйста, заполните все поля")
            return
        }
        setLoading(true)
        simulateLogin(username, password)
    }

    private fun simulateLogin(username: String, password: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            setLoading(false)
            // В демо-режиме пропускаем любого с непустым паролем
            if (username.isNotEmpty() && password.isNotEmpty()) {
                saveUserCredentials(username, generateUserId())
                showSuccess("Вход выполнен успешно!")
                startMainActivity()
            } else {
                showError("Неверный логин или пароль")
            }
        }, 1500)
    }

    private fun loginAsGuest() {
        saveUserCredentials("Гость", 0)
        showSuccess("Добро пожаловать, Гость!")
        startMainActivity()
    }

    private fun saveUserCredentials(username: String, userId: Int) {
        with(sharedPreferences.edit()) {
            putString("username", username)
            putInt("userId", userId)
            putBoolean("isLoggedIn", true)
            apply()
        }
    }

    private fun generateUserId(): Int = (1..1000).random()

    private fun isUserLoggedIn(): Boolean = sharedPreferences.getBoolean("isLoggedIn", false)
    private fun getCurrentUsername(): String = sharedPreferences.getString("username", "") ?: ""

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun setLoading(loading: Boolean) {
        loadingIndicator.visibility = if (loading) android.view.View.VISIBLE else android.view.View.GONE
        loginButton.isEnabled = !loading
        registerButton.isEnabled = !loading
        skipButton.isEnabled = !loading
    }

    private fun showError(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    private fun showSuccess(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}