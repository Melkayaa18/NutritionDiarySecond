package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.yourname.nutritiondiarysecond.R

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

        // Проверяем, не вошел ли пользователь ранее
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
        loginButton.setOnClickListener {
            attemptLogin()
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        skipButton.setOnClickListener {
            loginAsGuest()
        }
    }

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
        android.os.Handler().postDelayed({
            setLoading(false)

            if (isValidCredentials(username, password)) {
                saveUserCredentials(username)
                showSuccess("Вход выполнен успешно!")
                startMainActivity()
            } else {
                showError("Неверный логин или пароль")
            }
        }, 1500)
    }

    private fun isValidCredentials(username: String, password: String): Boolean {
        // Временная проверка - любой непустой пароль считается верным
        // В реальном приложении здесь будет проверка через API
        return username.isNotEmpty() && password.isNotEmpty()
    }

    private fun loginAsGuest() {
        saveUserCredentials("Гость")
        showSuccess("Добро пожаловать, Гость!")
        startMainActivity()
    }

    private fun saveUserCredentials(username: String) {
        with(sharedPreferences.edit()) {
            putString("username", username)
            putBoolean("isLoggedIn", true)
            if (username != "Гость") {
                putInt("userId", generateUserId())
            }
            apply()
        }
    }

    private fun generateUserId(): Int {
        return (1..1000).random()
    }

    private fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun getCurrentUsername(): String {
        return sharedPreferences.getString("username", "") ?: ""
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun setLoading(loading: Boolean) {
        loadingIndicator.visibility = if (loading) android.view.View.VISIBLE else android.view.View.GONE
        loginButton.isEnabled = !loading
        registerButton.isEnabled = !loading
        skipButton.isEnabled = !loading
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        animateShake(loginButton)
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun animateShake(view: android.view.View) {
        view.animate()
            .translationXBy(20f)
            .setDuration(50)
            .withEndAction {
                view.animate()
                    .translationXBy(-40f)
                    .setDuration(50)
                    .withEndAction {
                        view.animate()
                            .translationXBy(20f)
                            .setDuration(50)
                            .start()
                    }
                    .start()
            }
            .start()
    }
}