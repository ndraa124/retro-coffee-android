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

    @GET("product/promo")
    suspend fun getAllProductByPromo(
        @Query("search") search: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null
    ): ProductResponse

    @GET("product")
    suspend fun getAllProduct() : ProductResponse

    @GET("product/filter/higher")
    suspend fun getProductByHigher() : ProductResponse

    @GET("product/filter/lower")
    suspend fun getProductByLower() : ProductResponse

    @GET("product")
    suspend fun searchProductByName(@Query("search") productName:String) : ProductResponse

    @GET("product/filter/category")
    suspend fun searchProductByCategory(@Query("search") productName:String) : ProductResponse

    @GET("product/filter/higher")
    suspend fun searchProductByHigher(@Query("search") productName:String) : ProductResponse

    @GET("product/filter/lower")
    suspend fun searchProductByLower(@Query("search") productName:String) : ProductResponse

}