package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.models.Product

class AddCustomProductActivity : AppCompatActivity() {

    private lateinit var productNameEntry: EditText
    private lateinit var caloriesEntry: EditText
    private lateinit var proteinEntry: EditText
    private lateinit var fatEntry: EditText
    private lateinit var carbsEntry: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var barcodeEntry: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var loadingIndicator: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_custom_product)

        initializeViews()
        setupClickListeners()
        setupCategorySpinner()
    }

    private fun initializeViews() {
        productNameEntry = findViewById(R.id.productNameEntry)
        caloriesEntry = findViewById(R.id.caloriesEntry)
        proteinEntry = findViewById(R.id.proteinEntry)
        fatEntry = findViewById(R.id.fatEntry)
        carbsEntry = findViewById(R.id.carbsEntry)
        categorySpinner = findViewById(R.id.categorySpinner)
        barcodeEntry = findViewById(R.id.barcodeEntry)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        loadingIndicator = findViewById(R.id.loadingIndicator)
    }

    private fun setupClickListeners() {
        saveButton.setOnClickListener {
            attemptSaveProduct()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun setupCategorySpinner() {
        val categories = arrayOf(
            "Фрукты", "Овощи", "Мясо", "Рыба", "Молочные продукты",
            "Зерновые", "Напитки", "Сладости", "Другое"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }

    private fun attemptSaveProduct() {
        val name = productNameEntry.text.toString().trim()
        val caloriesText = caloriesEntry.text.toString()
        val proteinText = proteinEntry.text.toString()
        val fatText = fatEntry.text.toString()
        val carbsText = carbsEntry.text.toString()
        val barcode = barcodeEntry.text.toString().trim()

        if (!validateInputs(name, caloriesText, proteinText, fatText, carbsText)) {
            return
        }

        setLoading(true)
        saveCustomProduct(
            name,
            caloriesText.toDouble(),
            proteinText.toDouble(),
            fatText.toDouble(),
            carbsText.toDouble(),
            categorySpinner.selectedItemPosition + 1, // CategoryId начинается с 1
            barcode
        )
    }

    private fun validateInputs(
        name: String,
        caloriesText: String,
        proteinText: String,
        fatText: String,
        carbsText: String
    ): Boolean {
        val errors = mutableListOf<String>()

        if (name.isEmpty()) errors.add("Введите название продукта")
        if (caloriesText.isEmpty() || caloriesText.toDoubleOrNull() == null || caloriesText.toDouble() < 0)
            errors.add("Введите корректную калорийность")
        if (proteinText.isEmpty() || proteinText.toDoubleOrNull() == null || proteinText.toDouble() < 0)
            errors.add("Введите корректное количество белка")
        if (fatText.isEmpty() || fatText.toDoubleOrNull() == null || fatText.toDouble() < 0)
            errors.add("Введите корректное количество жиров")
        if (carbsText.isEmpty() || carbsText.toDoubleOrNull() == null || carbsText.toDouble() < 0)
            errors.add("Введите корректное количество углеводов")

        if (errors.isNotEmpty()) {
            showError(errors.joinToString("\n"))
            return false
        }

        return true
    }

    private fun saveCustomProduct(
        name: String,
        calories: Double,
        protein: Double,
        fat: Double,
        carbs: Double,
        categoryId: Int,
        barcode: String
    ) {
        // Временная заглушка - сохранение продукта
        android.os.Handler().postDelayed({
            setLoading(false)

            // В реальном приложении здесь будет вызов API
            val newProduct = Product(
                productId = generateProductId(),
                name = name,
                caloriesPer100g = calories,
                proteinPer100g = protein,
                fatPer100g = fat,
                carbsPer100g = carbs,
                categoryId = categoryId,
                isCustom = true,
                createdByUserId = getCurrentUserId(),
                barcode = barcode
            )

            showSuccess("Продукт \"$name\" успешно создан!")

            // Возвращаем результат в DiaryEntryActivity - ИСПРАВЛЕННАЯ ЧАСТЬ
            val resultIntent = Intent()
            resultIntent.putExtra("newProduct", newProduct) // Теперь работает, т.к. Product - Parcelable
            setResult(RESULT_OK, resultIntent)
            finish()
        }, 1500)
    }

    private fun generateProductId(): Int {
        return (10000..99999).random()
    }

    private fun getCurrentUserId(): Int {
        val sharedPreferences = getSharedPreferences("NutritionDiary", MODE_PRIVATE)
        return sharedPreferences.getInt("userId", 0)
    }

    private fun setLoading(loading: Boolean) {
        loadingIndicator.visibility = if (loading) android.view.View.VISIBLE else android.view.View.GONE
        saveButton.isEnabled = !loading
        cancelButton.isEnabled = !loading

        if (loading) {
            saveButton.text = "Сохранение..."
        } else {
            saveButton.text = "Сохранить продукт"
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}