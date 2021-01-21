package com.id124.retrocoffee.model.order

data class OrderResponse(
    val success: Boolean,
    val message: String,
    val data: List<OrderModel>
)