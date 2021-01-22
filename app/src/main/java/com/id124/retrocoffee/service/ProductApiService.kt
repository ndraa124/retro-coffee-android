package com.id124.retrocoffee.service

import com.id124.retrocoffee.model.product.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApiService {

    @GET("product")
    suspend fun getAllProduct() : ProductResponse

    @GET("product")
    suspend fun searchProductByName(@Query("search") productName:String) : ProductResponse
    
}