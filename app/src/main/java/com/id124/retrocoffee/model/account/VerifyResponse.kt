package com.id124.retrocoffee.model.account

import com.google.gson.annotations.SerializedName

data class VerifyResponse(val message: String, val success: Boolean, val data: VerifyData) {
    data class VerifyData(
        @SerializedName("ac_id")
        val acid: Int
    )
}