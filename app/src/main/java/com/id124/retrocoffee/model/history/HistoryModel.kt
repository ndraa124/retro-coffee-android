package com.id124.retrocoffee.model.history

import com.google.gson.annotations.SerializedName

data class HistoryModel(
    val htId: Int?,
    val csId: Int?,
    val orId: String?,
    val htProduct: String?,
    val htPrice: String?,
    val htQty: String?,
    val htTotal: String?
)
