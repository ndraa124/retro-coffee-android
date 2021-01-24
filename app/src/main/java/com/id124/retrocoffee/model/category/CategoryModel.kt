package com.id124.retrocoffee.model.category

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("ct_id")
    val ctId: Int,

    @SerializedName("ct_name")
    val ctName: String,

    @SerializedName("ct_status")
    val ctStatus: Int,

    @SerializedName("ct_pic_image")
    val ctPicImage: String
)
