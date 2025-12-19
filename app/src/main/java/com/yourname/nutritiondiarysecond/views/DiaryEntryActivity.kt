package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.adapters.ProductsAdapter
import com.yourname.nutritiondiarysecond.models.Product

class DiaryEntryActivity : AppCompatActivity() {

    // ВСЕ КОНСТАНТЫ В ОДНОМ COMPANION OBJECT
    companion object {
        private const val ADD_PRODUCT_REQUEST = 1001
        private const val BARCODE_SCAN_REQUEST = 1004
    }
    private lateinit var titleLabel: TextView
    private lateinit var productSearchBar: EditText
    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var selectedProductFrame: com.google.android.material.card.MaterialCardView
    private lateinit var selectedProductName: TextView
    private lateinit var selectedProductCalories: TextView
    private lateinit var editProductButton: Button
    private lateinit var quantityEntry: EditText
    private lateinit var caloriesLabel: TextView
    private lateinit var proteinLabel: TextView
    private lateinit var fatLabel: TextView
    private lateinit var carbsLabel: TextView
    private lateinit var addCustomProductButton: Button
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var scanBarcodeButton: Button
    private var mealTypeId: Int = 0
    private var mealTypeName: String = ""
    private var selectedProduct: Product? = null
    private val allProducts = mutableListOf<Product>()
    private val filteredProducts = mutableListOf<Product>()
    private lateinit var productsAdapter: ProductsAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_entry)

        // Получаем переданные данные
        mealTypeId = intent.getIntExtra("MEAL_TYPE_ID", 0)
        mealTypeName = intent.getStringExtra("MEAL_TYPE_NAME") ?: ""

        initializeViews()
        setupRecyclerView()
        setupClickListeners()
        loadProducts()
    }

    private fun initializeViews() {
        titleLabel = findViewById(R.id.titleLabel)
        productSearchBar = findViewById(R.id.productSearchBar)
        productsRecyclerView = findViewById(R.id.productsRecyclerView)
        selectedProductFrame = findViewById(R.id.selectedProductFrame)
        selectedProductName = findViewById(R.id.selectedProductName)
        selectedProductCalories = findViewById(R.id.selectedProductCalories)
        editProductButton = findViewById(R.id.editProductButton)
        quantityEntry = findViewById(R.id.quantityEntry)
        caloriesLabel = findViewById(R.id.caloriesLabel)
        proteinLabel = findViewById(R.id.proteinLabel)
        fatLabel = findViewById(R.id.fatLabel)
        carbsLabel = findViewById(R.id.carbsLabel)
        addCustomProductButton = findViewById(R.id.addCustomProductButton)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        scanBarcodeButton = findViewById(R.id.scanBarcodeButton)
        // Устанавливаем заголовок
        titleLabel.text = "Добавление продуктов для $mealTypeName"

        // Настройка поиска
        productSearchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterProducts(s.toString())
            }
        })

        // Настройка изменения количества
        quantityEntry.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                calculateNutrition()
            }
        })
    }


    private fun setupRecyclerView() {
        productsAdapter = ProductsAdapter(filteredProducts) { product ->
            onProductSelected(product)
        }
        productsRecyclerView.layoutManager = LinearLayoutManager(this)
        productsRecyclerView.adapter = productsAdapter
    }

    private fun setupClickListeners() {
        editProductButton.setOnClickListener {
            Toast.makeText(this, "Редактирование продукта будет доступно позже", Toast.LENGTH_SHORT).show()
        }

        addCustomProductButton.setOnClickListener {
            val intent = Intent(this, AddCustomProductActivity::class.java)
            startActivityForResult(intent, ADD_PRODUCT_REQUEST)
        }

        // Добавляем кнопку для сканирования штрих-кода
        val scanBarcodeButton: Button = findViewById(R.id.scanBarcodeButton)
        scanBarcodeButton?.setOnClickListener {
            val intent = Intent(this, BarcodeScannerActivity::class.java)
            startActivityForResult(intent, BARCODE_SCAN_REQUEST)
        }

        saveButton.setOnClickListener {
            saveEntry()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }
    // Добавьте метод для открытия сканера
    private fun openBarcodeScanner() {
        val intent = Intent(this, BarcodeScannerActivity::class.java)
        startActivityForResult(intent, BARCODE_SCAN_REQUEST)
    }

    // Обновите onActivityResult для обработки сканирования
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            ADD_PRODUCT_REQUEST -> {
                if (resultCode == RESULT_OK) {
                    val newProduct = data?.getParcelableExtra<Product>("newProduct")
                    newProduct?.let { product ->
                        allProducts.add(product)
                        filteredProducts.add(product)
                        productsAdapter.updateProducts(filteredProducts)
                        onProductSelected(product)
                        Toast.makeText(this, "Продукт \"${product.name}\" добавлен!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            BARCODE_SCAN_REQUEST -> {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Продукт из штрих-кода добавлен!", Toast.LENGTH_SHORT).show()
                    // Здесь можно обработать результат сканирования
                }
            }
        }
    }

    private fun loadProducts() {
        // Заглушка - временные данные
        allProducts.clear()
        allProducts.addAll(listOf(
            Product(1, "Яблоко", 52.0, 0.3, 0.2, 14.0, 1, false, 0),
            Product(2, "Банан", 89.0, 1.1, 0.3, 22.8, 1, false, 0),
            Product(3, "Куриная грудка", 165.0, 31.0, 3.6, 0.0, 2, false, 0),
            Product(4, "Овсянка", 389.0, 16.9, 6.9, 66.3, 3, false, 0),
            Product(5, "Молоко", 42.0, 3.4, 1.0, 4.8, 4, false, 0)
        ))
        filteredProducts.clear()
        filteredProducts.addAll(allProducts)
        productsAdapter.notifyDataSetChanged()
    }

    private fun filterProducts(searchText: String) {
        filteredProducts.clear()
        if (searchText.isEmpty()) {
            filteredProducts.addAll(allProducts)
        } else {
            val query = searchText.toLowerCase()
            allProducts.forEach { product ->
                if (product.name.toLowerCase().contains(query)) {
                    filteredProducts.add(product)
                }
            }
        }
        productsAdapter.notifyDataSetChanged()
    }

    private fun onProductSelected(product: Product) {
        selectedProduct = product
        updateSelectedProductDisplay()
        calculateNutrition()
    }

    private fun updateSelectedProductDisplay() {
        selectedProduct?.let { product ->
            selectedProductName.text = product.name
            selectedProductCalories.text = "${product.caloriesPer100g} ккал/100г"
            selectedProductFrame.visibility = android.view.View.VISIBLE
        } ?: run {
            selectedProductFrame.visibility = android.view.View.GONE
        }
    }

    private fun calculateNutrition() {
        val product = selectedProduct
        val quantityText = quantityEntry.text.toString()

        if (product == null || quantityText.isEmpty()) {
            resetNutritionLabels()
            return
        }

        try {
            val grams = quantityText.toDouble()
            if (grams <= 0) {
                resetNutritionLabels()
                return
            }

            val ratio = grams / 100.0
            val calories = product.caloriesPer100g * ratio
            val protein = product.proteinPer100g * ratio
            val fat = product.fatPer100g * ratio
            val carbs = product.carbsPer100g * ratio

            updateNutritionLabels(calories, protein, fat, carbs)
        } catch (e: NumberFormatException) {
            resetNutritionLabels()
        }
    }

    private fun updateNutritionLabels(calories: Double, protein: Double, fat: Double, carbs: Double) {
        caloriesLabel.text = "%.1f".format(calories)
        proteinLabel.text = "%.1f г".format(protein)
        fatLabel.text = "%.1f г".format(fat)
        carbsLabel.text = "%.1f г".format(carbs)
    }

    private fun resetNutritionLabels() {
        caloriesLabel.text = "0"
        proteinLabel.text = "0 г"
        fatLabel.text = "0 г"
        carbsLabel.text = "0 г"
    }

    private fun saveEntry() {
        if (selectedProduct == null) {
            Toast.makeText(this, "Выберите продукт", Toast.LENGTH_SHORT).show()
            return
        }

        val quantityText = quantityEntry.text.toString()
        if (quantityText.isEmpty()) {
            Toast.makeText(this, "Введите количество", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val grams = quantityText.toDouble()
            if (grams <= 0) {
                Toast.makeText(this, "Количество должно быть больше 0", Toast.LENGTH_SHORT).show()
                return
            }

            // Здесь будет сохранение в базу данных
            Toast.makeText(this, "Запись сохранена!", Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Введите корректное количество", Toast.LENGTH_SHORT).show()
        }
    }
}