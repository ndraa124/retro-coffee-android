package com.id124.retrocoffee.model.product

import com.google.gson.annotations.SerializedName

data class ProductModel(
    @SerializedName("pr_id")
    val prId: Int,
    @SerializedName("ct_id")
    val ctId: Int,
    @SerializedName("ct_pic_image")
    val ctImage: String,
    @SerializedName("pr_name")
    val prName: String,
    @SerializedName("pr_price")
    val prPrice: String,
    @SerializedName("pr_desc")
    val prDesc: String,
    @SerializedName("pr_status")
    val prStatus: String,
    @SerializedName("pr_pic_image")
    val prImage: String,
    @SerializedName("pr_updated_at")
    val prUpdateStamp: String,
)
