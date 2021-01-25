package com.id124.retrocoffee.service

import com.id124.retrocoffee.model.cart.CartResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}