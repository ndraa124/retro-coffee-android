package com.id124.retrocoffee.model.history

data class HistoryResponse(
    val success: Boolean,
    val message: String,
    val data: List<HistoryModel>
)