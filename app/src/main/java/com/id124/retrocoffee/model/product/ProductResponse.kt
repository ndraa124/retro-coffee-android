package com.id124.retrocoffee.model.product

data class ProductResponse(
    val success: Boolean,
    val message: String,
    val data: List<ProductModel>
)