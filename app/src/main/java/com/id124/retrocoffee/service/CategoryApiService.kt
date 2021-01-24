package com.id124.retrocoffee.service

import com.id124.retrocoffee.model.category.CategoryResponse
import retrofit2.http.GET

interface CategoryApiService {
    @GET("category")
    suspend fun getAllCategory(): CategoryResponse
}