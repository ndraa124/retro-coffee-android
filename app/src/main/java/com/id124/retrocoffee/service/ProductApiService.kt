package com.id124.retrocoffee.service

import com.id124.retrocoffee.model.product.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    @GET("product/category/{ctId}")
    suspend fun getAllProductByCategory(
        @Path("ctId") ctId: Int,
        @Query("search") search: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null
    ): ProductResponse
}