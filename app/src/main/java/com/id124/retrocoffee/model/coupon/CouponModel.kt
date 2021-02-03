package com.id124.retrocoffee.model.coupon

import com.google.gson.annotations.SerializedName

data class CouponModel(
    @SerializedName("cp_id")
    val cpId: Int,

    @SerializedName("cp_name")
    val cpName: String,

    @SerializedName("cp_price")
    val cpPriceDiscount: Int,

    @SerializedName("cp_desc")
    val cpDesc: String,

    @SerializedName("cp_pic_image")
    val cpPicImage: Int,

    @SerializedName("cp_color")
    val cpColor: Int
)
