package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.*
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.models.User
import java.util.*
import kotlin.math.abs

class RegistrationActivity : AppCompatActivity() {

    private lateinit var usernameEntry: EditText
    private lateinit var passwordEntry: EditText
    private lateinit var emailEntry: EditText
    private lateinit var ageEntry: EditText
    private lateinit var heightEntry: EditText
    private lateinit var currentWeightEntry: EditText
    private lateinit var desiredWeightEntry: EditText
    private lateinit var registerButton: Button
    private lateinit var backButton: Button
    private lateinit var loadingIndicator: ProgressBar

    private lateinit var sharedPreferences: SharedPreferences
    private val tempUsers = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        sharedPreferences = getSharedPreferences("NutritionDiary", MODE_PRIVATE)
        initializeViews()
        setupClickListeners()
        addTestUsers()
    }

    private fun initializeViews() {
        usernameEntry = findViewById(R.id.usernameEntry)
        passwordEntry = findViewById(R.id.passwordEntry)
        emailEntry = findViewById(R.id.emailEntry)
        ageEntry = findViewById(R.id.ageEntry)
        heightEntry = findViewById(R.id.heightEntry)
        currentWeightEntry = findViewById(R.id.currentWeightEntry)
        desiredWeightEntry = findViewById(R.id.desiredWeightEntry)
        registerButton = findViewById(R.id.registerButton)
        backButton = findViewById(R.id.backButton)
        loadingIndicator = findViewById(R.id.loadingIndicator)
    }

    private fun setupClickListeners() {
        registerButton.setOnClickListener { attemptRegistration() }
        backButton.setOnClickListener { onBackPressed() }
    }

    private fun attemptRegistration() {
        animateButtonClick(registerButton)

        val username = usernameEntry.text.toString().trim()
        val password = passwordEntry.text.toString()
        val email = emailEntry.text.toString().trim()
        val age = ageEntry.text.toString()
        val height = heightEntry.text.toString()
        val currentWeight = currentWeightEntry.text.toString()
        val desiredWeight = desiredWeightEntry.text.toString()

        if (!validateInputs(username, password, email, age, height, currentWeight, desiredWeight)) return

        setLoading(true)
        simulateRegistration(username, password, email, age.toInt(), height.toDouble(), currentWeight.toDouble(), desiredWeight.toDouble())
    }

    private fun validateInputs(
        username: String, password: String, email: String,
        age: String, height: String, currentWeight: String, desiredWeight: String
    ): Boolean {
        val errors = mutableListOf<String>()
        if (username.isEmpty()) errors.add("Введите логин")
        if (password.isEmpty()) errors.add("Введите пароль")
        if (email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            errors.add("Введите корректный email")
        if (age.isEmpty() || age.toIntOrNull() == null || age.toInt() <= 0 || age.toInt() > 120)
            errors.add("Введите корректный возраст (1-120 лет)")
        if (height.isEmpty() || height.toDoubleOrNull() == null || height.toDouble() <= 0 || height.toDouble() > 250)
            errors.add("Введите корректный рост (1-250 см)")
        if (currentWeight.isEmpty() || currentWeight.toDoubleOrNull() == null || currentWeight.toDouble() <= 0 || currentWeight.toDouble() > 500)
            errors.add("Введите корректный текущий вес")
        if (desiredWeight.isEmpty() || desiredWeight.toDoubleOrNull() == null || desiredWeight.toDouble() <= 0 || desiredWeight.toDouble() > 500)
            errors.add("Введите корректный желаемый вес")

        if (errors.isNotEmpty()) {
            showError(errors.joinToString("\n"))
            return false
        }
        return true
    }

    private fun simulateRegistration(
        username: String, password: String, email: String,
        age: Int, height: Double, currentWeight: Double, desiredWeight: Double
    ) {
        Handler(Looper.getMainLooper()).postDelayed({
            setLoading(false)
            if (isUsernameAvailable(username)) {
                saveUserRegistration(username, password, email, age, height, currentWeight, desiredWeight)
                showSuccess("Регистрация прошла успешно!")
                navigateToLogin()
            } else {
                showError("Пользователь с таким логином уже существует")
                animateShake(usernameEntry)
            }
        }, 1500)
    }

    private fun isUsernameAvailable(username: String): Boolean {
        return tempUsers.none { it.username == username } &&
                sharedPreferences.getString("username", null) != username
    }

    private fun saveUserRegistration(
        username: String, password: String, email: String,
        age: Int, height: Double, currentWeight: Double, desiredWeight: Double
    ) {
        with(sharedPreferences.edit()) {
            putString("username", username)
            putString("email", email)
            putInt("age", age)
            putFloat("height", height.toFloat())
            putFloat("currentWeight", currentWeight.toFloat())
            putFloat("desiredWeight", desiredWeight.toFloat())
            putInt("userId", generateUserId())
            putBoolean("isLoggedIn", true)
            apply()
        }
        val newUser = User(
            userId = generateUserId(),
            username = username,
            password = password,
            email = email,
            age = age,
            height = height,
            currentWeight = currentWeight,
            desiredWeight = desiredWeight,
            dailyCalorieGoal = calculateDailyCalories(age, height, currentWeight, desiredWeight),
            registrationDate = Date()
        )
        tempUsers.add(newUser)
    }

    private fun calculateDailyCalories(age: Int, height: Double, currentWeight: Double, desiredWeight: Double): Int {
        val baseCalories = when {
            desiredWeight < currentWeight -> 1800
            desiredWeight > currentWeight -> 2200
            else -> 2000
        }
        val adjustedCalories = baseCalories + (height - 160).toInt() * 5 - (age - 25) * 5
        return adjustedCalories.coerceIn(1200, 3000)
    }

    private fun generateUserId(): Int = abs(UUID.randomUUID().hashCode()) % 100000

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun setLoading(loading: Boolean) {
        loadingIndicator.visibility = if (loading) android.view.View.VISIBLE else android.view.View.GONE
        registerButton.isEnabled = !loading
        backButton.isEnabled = !loading
        registerButton.text = if (loading) "Регистрация..." else getString(R.string.register_button)
    }

    private fun showError(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    private fun showSuccess(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun animateButtonClick(button: Button) {
        button.animate().scaleX(0.95f).scaleY(0.95f).setDuration(50).withEndAction {
            button.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
        }.start()
    }

    private fun animateShake(view: android.view.View) {
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        view.startAnimation(shake)
    }

    private fun addTestUsers() {
        tempUsers.add(User(1, "testuser", "password", "test@example.com", 25, 170.0, 65.0, 60.0, 2000, Date()))
    }
}