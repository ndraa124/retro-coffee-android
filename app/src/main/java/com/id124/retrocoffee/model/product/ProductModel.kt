package com.id124.retrocoffee.model.product

import com.google.gson.annotations.SerializedName

data class ProductModel(
    @SerializedName("pr_id")
    val prId: Int
)
