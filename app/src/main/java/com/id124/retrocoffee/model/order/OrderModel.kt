package com.id124.retrocoffee.model.order

data class OrderModel(
    val orId: Int?,
    val csId: Int?,
    val orTotal: String?,
    val orAddress: String?,
    val orLatitude: String?,
    val orLongitude: String?,
    val orStatus: Int?,
    val orNoteCancel: String?,
    val orNoteApprove: String?,
    val orMethodPayment: String?,
    val orFee: String?,
    val orDate: String?,
)
