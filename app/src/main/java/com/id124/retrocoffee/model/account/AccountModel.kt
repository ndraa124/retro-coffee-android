package com.id124.retrocoffee.model.account

import com.google.gson.annotations.SerializedName

data class AccountModel(
    @SerializedName("ac_id")
    val acId: String
)
