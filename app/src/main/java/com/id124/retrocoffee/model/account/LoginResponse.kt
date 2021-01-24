package com.id124.retrocoffee.model.account

import com.google.gson.annotations.SerializedName

data class LoginResponse(val success: Boolean, val message: String, val data: AccountItem) {
    data class AccountItem(
        @SerializedName("ac_id")
        val acId: Int,

        @SerializedName("ac_name")
        val acName: String,

        @SerializedName("ac_email")
        val acEmail: String,

        @SerializedName("ac_phone")
        val acPhone: String,

        @SerializedName("ac_password")
        val acPassword: String,

        @SerializedName("ac_level")
        val acLevel: Int,

        @SerializedName("token")
        val token: String,

        @SerializedName("exp")
        val expired: Long,

        @SerializedName("cs_id")
        val csId: Int


    )
}