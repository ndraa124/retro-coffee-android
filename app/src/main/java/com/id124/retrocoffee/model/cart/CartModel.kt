package com.id124.retrocoffee.model.cart

import com.google.gson.annotations.SerializedName

data class CartModel(
    @SerializedName("cr_id")
    val crId: Int,

    @SerializedName("cs_id")
    val csId: Int,

    @SerializedName("cr_product")
    val crProduct: String,

    @SerializedName("cr_price")
    val crPrice: Long,

    @SerializedName("cr_qty")
    val crQty: Int,

    @SerializedName("cr_total")
    val crTotal: Long,

    @SerializedName("cr_expired")
    val crExpired: Int,

    @SerializedName("cr_pic_image")
    val crPicImage: String? = null
)
