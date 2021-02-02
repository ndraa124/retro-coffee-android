package com.id124.retrocoffee.model.order

import com.google.gson.annotations.SerializedName

data class OrderResponse(val success: Boolean, val message: String, val data: List<Order>?) {
    data class Order(
        @SerializedName("or_id")
        val orderId: Int,

        @SerializedName("cs_id")
        val customerId: Int,

        @SerializedName("or_pay_total")
        val orderPayTotal: String,

        @SerializedName("or_address")
        val orderAddress: String,

        @SerializedName("or_latitude")
        val orderLatitude: String,

        @SerializedName("or_longitude")
        val orderLongitude: String,

        @SerializedName("or_status")
        val orderStatus: Int,

        @SerializedName("or_note_cancel")
        val orderNoteCancel: String,

        @SerializedName("or_note_approve")
        val orderNoteApprove: String,

        @SerializedName("or_method_payment")
        val orderMethodPayment: String,

        @SerializedName("or_fee")
        val orderFee: String,

        @SerializedName("or_date_order")
        val orderDate: String
    )
}