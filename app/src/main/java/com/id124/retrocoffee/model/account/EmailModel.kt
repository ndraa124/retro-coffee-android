package com.id124.retrocoffee.model.account

import com.google.gson.annotations.SerializedName

//data class EmailModel()

data class EmailModel(
    val message: String,
    val success: Boolean,
    val `data`: EmailResponse)
{
    data class EmailResponse(
        @SerializedName("ac_id") val acid: Int,
        @SerializedName("ac_name") val acname: String,
        @SerializedName("ac_email") val acemail: String,
        @SerializedName("ac_phone") val acphone: String,
        @SerializedName("ac_password") val acpassword: String,
        @SerializedName("ac_level") val aclevel: Int,
        @SerializedName("ac_status") val acstatus: Int,
        @SerializedName("ac_created_at") val accreatedat: String,
        @SerializedName("ac_updated_at") val acupdatedat: String
    )
}