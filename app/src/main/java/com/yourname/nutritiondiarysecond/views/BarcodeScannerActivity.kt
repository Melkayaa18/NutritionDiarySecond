package com.yourname.nutritiondiarysecond.views

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.yourname.nutritiondiarysecond.R
import com.yourname.nutritiondiarysecond.models.Product
import java.util.concurrent.Executors

class BarcodeScannerActivity : AppCompatActivity() {

    private lateinit var cameraPreview: androidx.camera.view.PreviewView
    private lateinit var scannerLine: View
    private lateinit var backButton: Button
    private lateinit var instructionText: TextView
    private lateinit var manualEntryButton: Button
    private lateinit var resultCard: com.google.android.material.card.MaterialCardView
    private lateinit var resultTitle: TextView
    private lateinit var resultProductName: TextView
    private lateinit var resultProductInfo: TextView
    private lateinit var addProductButton: Button
    private lateinit var scanAgainButton: Button

    private var cameraProvider: ProcessCameraProvider? = null
    private var isScanning = true
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 1001
        private const val TAG = "BarcodeScanner"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scanner)

        initializeViews()
        setupClickListeners()
        startScannerLineAnimation()

        // Запрашиваем разрешение камеры
        if (hasCameraPermission()) {
            startCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun initializeViews() {
        cameraPreview = findViewById(R.id.cameraPreview)
        scannerLine = findViewById(R.id.scannerLine)
        backButton = findViewById(R.id.backButton)
        instructionText = findViewById(R.id.instructionText)
        manualEntryButton = findViewById(R.id.manualEntryButton)
        resultCard = findViewById(R.id.resultCard)
        resultTitle = findViewById(R.id.resultTitle)
        resultProductName = findViewById(R.id.resultProductName)
        resultProductInfo = findViewById(R.id.resultProductInfo)
        addProductButton = findViewById(R.id.addProductButton)
        scanAgainButton = findViewById(R.id.scanAgainButton)
    }

    private fun setupClickListeners() {
        backButton.setOnClickListener {
            finish()
        }

        manualEntryButton.setOnClickListener {
            showManualEntryDialog()
        }

        addProductButton.setOnClickListener {
            addScannedProductToDiary()
        }

        scanAgainButton.setOnClickListener {
            resetScanner()
        }
    }

    private fun startScannerLineAnimation() {
        scannerLine.animate()
            .translationY(140f)
            .setDuration(1500)
            .withEndAction {
                scannerLine.animate()
                    .translationY(0f)
                    .setDuration(1500)
                    .withEndAction {
                        if (isScanning) {
                            startScannerLineAnimation()
                        }
                    }
                    .start()
            }
            .start()
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "Для сканирования нужен доступ к камере", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases() {
        val cameraProvider = cameraProvider ?: return

        // Предпросмотр камеры
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(cameraPreview.surfaceProvider)
        }

        // Анализ изображения для сканирования штрих-кодов
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()

        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
            val mediaImage = imageProxy.image
            if (mediaImage != null && isScanning) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                barcodeScanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            when (barcode.valueType) {
                                Barcode.TYPE_PRODUCT -> {
                                    val barcodeValue = barcode.rawValue
                                    if (barcodeValue != null) {
                                        // Найден штрих-код продукта
                                        runOnUiThread {
                                            handleBarcodeResult(barcodeValue)
                                        }
                                    }
                                }
                                else -> {
                                    // Другие типы штрих-кодов
                                    Log.d(TAG, "Other barcode type: ${barcode.valueType}")
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Barcode scanning failed", exception)
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
        }

        // Привязываем камеру к lifecycle
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalysis
            )
        } catch (exception: Exception) {
            Log.e(TAG, "Use case binding failed", exception)
        }
    }

    private fun handleBarcodeResult(barcode: String) {
        isScanning = false
        scannerLine.clearAnimation()

        // Имитация поиска продукта по базе данных
        val foundProduct = findProductByBarcode(barcode)

        if (foundProduct != null) {
            showProductFound(foundProduct)
        } else {
            showProductNotFound(barcode)
        }
    }

    private fun findProductByBarcode(barcode: String): Product? {
        // Заглушка - в реальном приложении здесь будет поиск в базе данных
        val mockProducts = listOf(
            Product(1, "Яблоко Голден", 52.0, 0.3, 0.2, 14.0, 1, false, 0, "4601234567890"),
            Product(2, "Молоко Простоквашино", 42.0, 3.4, 1.0, 4.8, 4, false, 0, "4609876543210"),
            Product(3, "Хлеб Бородинский", 201.0, 6.8, 1.3, 40.7, 3, false, 0, "4605555555555")
        )

        return mockProducts.find { it.barcode == barcode }
    }

    private fun showProductFound(product: Product) {
        resultTitle.text = "Продукт найден! ✅"
        resultProductName.text = product.name
        resultProductInfo.text = "${product.caloriesPer100g} ккал/100г • Б:${product.proteinPer100g}г Ж:${product.fatPer100g}г У:${product.carbsPer100g}г"

        resultCard.visibility = android.view.View.VISIBLE
        instructionText.text = "Продукт распознан"
    }

    private fun showProductNotFound(barcode: String) {
        resultTitle.text = "Продукт не найден ❌"
        resultProductName.text = "Штрих-код: $barcode"
        resultProductInfo.text = "Данный продукт отсутствует в базе данных. Вы можете добавить его вручную."

        addProductButton.text = "➕ Добавить продукт"

        resultCard.visibility = android.view.View.VISIBLE
        instructionText.text = "Продукт не распознан"
    }

    private fun showManualEntryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_manual_barcode, null)
        val barcodeInput = dialogView.findViewById<EditText>(R.id.barcodeInput)

        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("Ввод штрих-кода")
            .setView(dialogView)
            .setPositiveButton("Поиск") { dialog, which ->
                val barcode = barcodeInput.text.toString().trim()
                if (barcode.isNotEmpty()) {
                    handleBarcodeResult(barcode)
                } else {
                    Toast.makeText(this, "Введите штрих-код", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }

    private fun addScannedProductToDiary() {
        // Здесь будет логика добавления продукта в дневник
        Toast.makeText(this, "Продукт добавлен в дневник", Toast.LENGTH_SHORT).show()

        val resultIntent = Intent()
        resultIntent.putExtra("barcodeScanned", true)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun resetScanner() {
        isScanning = true
        resultCard.visibility = android.view.View.GONE
        instructionText.text = "Наведите камеру на штрих-код продукта"
        startScannerLineAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        isScanning = false
        cameraExecutor.shutdown()
        cameraProvider?.unbindAll()
    }
}