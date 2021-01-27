package com.id124.retrocoffee.service

import com.id124.retrocoffee.model.order.OrderResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OrderApiService {
    @FormUrlEncoded
    @POST("order")
    suspend fun addOrder(
        @Field("cs_id") csId: Int,
        @Field("or_pay_total") orPayTotal: Long,
        @Field("or_address") orAddress: String,
        @Field("or_note_cancel") orNoteCancel: String,
        @Field("or_note_approve") orNoteApprove: String,
        @Field("or_method_payment") orMethodPayment: String,
        @Field("or_fee") orFee: Long,
        @Field("or_date_order") orDateOrder: String
    ): OrderResponse
}