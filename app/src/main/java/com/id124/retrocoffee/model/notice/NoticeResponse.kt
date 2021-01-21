package com.id124.retrocoffee.model.notice

data class NoticeResponse(
    val success: Boolean,
    val message: String,
    val data: List<NoticeModel>
)