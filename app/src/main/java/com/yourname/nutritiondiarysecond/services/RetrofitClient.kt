package com.yourname.nutritiondiarysecond.services

import com.yourname.nutritiondiarysecond.models.Product
import com.yourname.nutritiondiarysecond.models.User
import retrofit2.Response
import retrofit2.http.*
import java.util.*

// Временный сервис-заглушка
class MockApiService : ApiService {

    private val mockUsers = mutableListOf<User>()
    private val mockProducts = mutableListOf<Product>()

    init {
        // Добавляем тестовые данные
        mockProducts.addAll(listOf(
            Product(1, "Яблоко", 52.0, 0.3, 0.2, 14.0, 1, false, 0),
            Product(2, "Банан", 89.0, 1.1, 0.3, 22.8, 1, false, 0),
            Product(3, "Куриная грудка", 165.0, 31.0, 3.6, 0.0, 2, false, 0)
        ))
    }

    override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        // Заглушка для входа
        return Response.success(LoginResponse(1, loginRequest.username))
    }

    override suspend fun register(registerRequest: RegisterRequest): Response<RegisterResponse> {
        // Заглушка для регистрации
        return Response.success(RegisterResponse(true, "Успешная регистрация"))
    }

    override suspend fun getProducts(): Response<List<Product>> {
        // Заглушка для получения продуктов
        return Response.success(mockProducts)
    }

    override suspend fun addDiaryEntry(entryRequest: DiaryEntryRequest): Response<Boolean> {
        // Заглушка для добавления записи
        return Response.success(true)
    }

    override suspend fun getDailySummary(userId: Int): Response<DailySummaryResponse> {
        // Заглушка для получения статистики
        return Response.success(DailySummaryResponse(0.0, 0.0, 0.0, 0.0))
    }
}

// Обновляем RetrofitClient для использования заглушки
object RetrofitClient {
    val apiService: ApiService = MockApiService()
}