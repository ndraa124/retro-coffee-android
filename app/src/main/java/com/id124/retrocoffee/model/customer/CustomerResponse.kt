package com.id124.retrocoffee.model.customer

data class CustomerResponse(
    val success: Boolean,
    val message: String,
    val data: List<CustomerModel>
)