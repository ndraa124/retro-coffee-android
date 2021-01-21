package com.id124.retrocoffee.model.order

import com.google.gson.annotations.SerializedName

data class OrderModel(
    @SerializedName("or_id")
    val orId: Int
)
