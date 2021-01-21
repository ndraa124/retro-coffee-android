package com.id124.retrocoffee.model.customer

import com.google.gson.annotations.SerializedName

data class CustomerModel(
    @SerializedName("cs_id")
    val csId: Int
)
