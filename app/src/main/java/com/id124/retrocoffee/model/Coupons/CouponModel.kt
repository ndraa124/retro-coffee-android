package com.id124.retrocoffee.model.Coupons

import com.google.gson.annotations.SerializedName

data class CouponModel(
    @SerializedName("cp_id")
    val cpId: Int,

    @SerializedName("cp_name")
    val cpName: String,

    @SerializedName("ct_desc")
    val cpDesc: Int,

)
