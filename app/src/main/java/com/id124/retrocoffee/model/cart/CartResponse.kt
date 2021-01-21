package com.id124.retrocoffee.model.cart

data class CartResponse(
    val success: Boolean,
    val message: String,
    val data: List<CartModel>
)