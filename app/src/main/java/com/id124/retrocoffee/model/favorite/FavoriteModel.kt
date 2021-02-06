package com.id124.retrocoffee.model.favorite

import com.google.gson.annotations.SerializedName

data class FavoriteModel(
    @SerializedName("fa_id")
    val faId: Int,
    @SerializedName("cs_id")
    val csId: Int,
    @SerializedName("pr_id")
    val prID: Int,
    @SerializedName("pr_name")
    val prName: String,
    @SerializedName("pr_price")
    val prPrice: String,
    @SerializedName("pr_desc")
    val prDesc: String,
    @SerializedName("pr_pic_image")
    val prPic: String
)
