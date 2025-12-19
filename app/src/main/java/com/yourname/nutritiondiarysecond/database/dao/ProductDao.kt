package com.yourname.nutritiondiarysecond.database.dao

import androidx.room.*
import com.yourname.nutritiondiarysecond.database.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("SELECT * FROM Products WHERE productId = :productId")
    suspend fun getProductById(productId: Int): ProductEntity?

    @Query("SELECT * FROM Products WHERE barcode = :barcode")
    suspend fun getProductByBarcode(barcode: String): ProductEntity?

    @Query("SELECT * FROM Products WHERE name LIKE '%' || :search || '%'")
    fun searchProducts(search: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM Products WHERE categoryId = :categoryId")
    fun getProductsByCategory(categoryId: Int): Flow<List<ProductEntity>>

    @Query("SELECT * FROM Products WHERE isCustom = 1 AND createdByUserId = :userId")
    fun getCustomProducts(userId: Int): Flow<List<ProductEntity>>

    @Query("SELECT * FROM Products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("DELETE FROM Products WHERE productId = :productId")
    suspend fun deleteProductById(productId: Int)

    // Для статистики
    @Query("SELECT COUNT(*) FROM Products")
    suspend fun getProductCount(): Int

    @Query("SELECT COUNT(*) FROM Products WHERE isCustom = 1")
    suspend fun getCustomProductCount(): Int
}