package com.id124.retrocoffee.model.product

import com.google.gson.annotations.SerializedName

data class ProductModel(
    @SerializedName("pr_id")
    val prId: Int,

    @SerializedName("ct_id")
    val ctId: Int,

    @SerializedName("pr_name")
    val prName: String,

    @SerializedName("pr_price")
    val prPrice: Long,

    @SerializedName("pr_desc")
    val prDesc: String,

    @SerializedName("pr_discount")
    val prDiscount: Int,

    @SerializedName("pr_discount_price")
    val prDiscountPrice: Long,

    @SerializedName("pr_is_discount")
    val prIsDiscount: Int,

    @SerializedName("pr_status")
    val prStatus: Int,

    @SerializedName("pr_pic_image")
    val prPicImage: String? = null,

    @SerializedName("ct_name")
    val ctName: String
)
