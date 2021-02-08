package com.id124.retrocoffee.model.order_admin

data class OrdersResponse(val success: Boolean, val message: String, val data: List<OrderModel>)