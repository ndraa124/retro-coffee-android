package com.id124.retrocoffee.model.account

data class AccountResponse(
    val success: Boolean,
    val message: String,
    val data: List<AccountModel>
)