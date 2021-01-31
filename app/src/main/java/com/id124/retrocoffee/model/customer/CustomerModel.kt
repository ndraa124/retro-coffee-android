package com.id124.retrocoffee.model.customer

import com.google.gson.annotations.SerializedName

data class CustomerModel(
    @SerializedName("cs_id") val csId: Int,
    @SerializedName("ac_id") val acId: Int,
    @SerializedName("cs_gender") val csGender: Int,
    @SerializedName("cs_dob") val csDob: String,
    @SerializedName("cs_address") val csAddress: String,
    @SerializedName("cs_latitude") val csLatitude: Int,
    @SerializedName("cs_longitude") val csLongitude: Int,
    @SerializedName("cs_pic_image") val csImage: String,
    @SerializedName("ac_name") val acName: String,
    @SerializedName("ac_email") val acEmail: String,
    @SerializedName("ac_phone") val acPhone: String
)
