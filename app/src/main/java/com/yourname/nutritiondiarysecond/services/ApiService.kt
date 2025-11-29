package com.yourname.nutritiondiarysecond.services

import com.yourname.nutritiondiarysecond.models.Product
import com.yourname.nutritiondiarysecond.models.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("api/products")
    suspend fun getProducts(): Response<List<Product>>

    @POST("api/diary/entries")
    suspend fun addDiaryEntry(@Body entryRequest: DiaryEntryRequest): Response<Boolean>

    @GET("api/users/{userId}/daily-summary")
    suspend fun getDailySummary(@Path("userId") userId: Int): Response<DailySummaryResponse>
}

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val userId: Int, val username: String)
data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val age: Int,
    val height: Double,
    val currentWeight: Double,
    val desiredWeight: Double
)
data class RegisterResponse(val success: Boolean, val message: String)
data class DiaryEntryRequest(
    val userId: Int,
    val mealTypeId: Int,
    val productId: Int,
    val quantity: Double,
    val calories: Double,
    val protein: Double,
    val fat: Double,
    val carbs: Double
)
data class DailySummaryResponse(
    val calories: Double,
    val protein: Double,
    val fat: Double,
    val carbs: Double
)