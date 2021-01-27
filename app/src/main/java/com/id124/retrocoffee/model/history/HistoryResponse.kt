package com.id124.retrocoffee.model.history

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    val success: Boolean,
    val message: String,
    val data: List<History>
) {
    data class History(@SerializedName("ht_id") val historyId: Int,
                          @SerializedName("cs_id") val customerId: Int,
                          @SerializedName("or_id") val orderId: String,
                          @SerializedName("ht_product") val historyProduct: String,
                          @SerializedName("ht_price") val historyPrice: String,
                          @SerializedName("ht_qty") val historyQty: String,
                          @SerializedName("ht_total") val historyTotal: String,
    )
}