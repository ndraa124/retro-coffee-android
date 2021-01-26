package com.id124.retrocoffee.service

import com.id124.retrocoffee.model.cart.CartResponse
import retrofit2.http.*

interface CartApiService {
    @FormUrlEncoded
    @POST("cart")
    suspend fun addCart(
        @Field("cs_id") csId: Int,
        @Field("cr_product") crProduct: String,
        @Field("cr_price") crPrice: Long,
        @Field("cr_qty") crQty: Int,
        @Field("cr_total") crTotal: Long,
        @Field("cr_expired") crExpired: Int,
        @Field("cr_pic_image") crPicImage: String,
    ): CartResponse

    @GET("cart/{csId}")
    suspend fun getAllCart(
        @Path("csId") csId: Int
    ): CartResponse
}