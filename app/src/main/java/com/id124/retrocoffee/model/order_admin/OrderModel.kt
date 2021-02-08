package com.id124.retrocoffee.model.order_admin

import com.google.gson.annotations.SerializedName
import com.id124.retrocoffee.model.history.HistoryModel

data class OrderModel(
    @SerializedName("or_id")
    val orId: Int,

    @SerializedName("cs_id")
    val csId: Int,

    @SerializedName("or_pay_total")
    val orPayTotal: String,

    @SerializedName("or_address")
    val orAddress: String,

    @SerializedName("or_latitude")
    val orLatitude: String? = null,

    @SerializedName("or_longitude")
    val orLongitude: String? = null,

    @SerializedName("or_status")
    val orStatus: Int,

    @SerializedName("or_note_cancel")
    val orNoteCancel: String,

    @SerializedName("or_note_approve")
    val orNoteApprove: String,

    @SerializedName("or_method_payment")
    val orMethodPayment: String,

    @SerializedName("or_fee")
    val orFee: String,

    @SerializedName("or_date_order")
    val orDateOrder: String,

    @SerializedName("history_product")
    val historyProduct: ArrayList<HistoryModel>
)
