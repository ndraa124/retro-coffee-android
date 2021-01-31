package com.id124.retrocoffee.model.account

import com.google.gson.annotations.SerializedName

//data class EmailModel()

data class CheckEmailResponse(val success: Boolean, val message: String, val data: Email) {
    data class Email(
        @SerializedName("ac_id")
        val acId: Int,
        @SerializedName("ac_name")
        val acName: String,
        @SerializedName("ac_email")
        val acEmail: String,
        @SerializedName("ac_phone")
        val acPhone: String,
        @SerializedName("ac_level")
        val acLevel: Int,
        @SerializedName("ac_status")
        val acStatus: Int,
    )
}