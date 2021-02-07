package com.id124.retrocoffee.service

import com.id124.retrocoffee.model.product.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ProductApiService {
    @Multipart
    @POST("product")
    suspend fun addProduct(
        @Part("ct_id") ctId: RequestBody,
        @Part("pr_name") prName: RequestBody,
        @Part("pr_price") prPrice: RequestBody,
        @Part("pr_desc") prDesc: RequestBody,
        @Part("pr_discount") prDiscount: RequestBody,
        @Part("pr_discount_price") prDiscountPrice: RequestBody,
        @Part("pr_is_discount") prIsDiscount: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): ProductResponse

    @Multipart
    @PUT("product/{prId}")
    suspend fun updateProduct(
        @Path("prId") prId: Int,
        @Part("ct_id") ctId: RequestBody,
        @Part("pr_name") prName: RequestBody,
        @Part("pr_price") prPrice: RequestBody,
        @Part("pr_desc") prDesc: RequestBody,
        @Part("pr_discount") prDiscount: RequestBody,
        @Part("pr_discount_price") prDiscountPrice: RequestBody,
        @Part("pr_is_discount") prIsDiscount: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): ProductResponse

    @DELETE("product/{prId}")
    suspend fun deleteProduct(
        @Path("prId") prId: Int
    ): ProductResponse

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

    @GET("product/filter/name")
    suspend fun getAllProduct() : ProductResponse

    @GET("product/filter/higher")
    suspend fun getProductByHigher() : ProductResponse

    @GET("product/filter/lower")
    suspend fun getProductByLower() : ProductResponse

    @GET("product/filter/name")
    suspend fun searchProductByName(@Query("search") productName:String) : ProductResponse

    @GET("product/filter/category")
    suspend fun searchProductByCategory(@Query("search") productName:String) : ProductResponse

    @GET("product/filter/higher")
    suspend fun searchProductByHigher(@Query("search") productName:String) : ProductResponse

    @GET("product/filter/lower")
    suspend fun searchProductByLower(@Query("search") productName:String) : ProductResponse

}