package com.id124.retrocoffee.service

import com.id124.retrocoffee.model.cart.CartResponse
import com.id124.retrocoffee.model.order.OrderResponse
import com.id124.retrocoffee.model.order_admin.OrdersResponse
import retrofit2.http.*

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

    @GET("order/{csId}")
    suspend fun getAllOrder(
        @Path("csId") csId: Int,
    ): OrderResponse

    @GET("order/orders/all")
    suspend fun getAllOrderCustomer(): OrdersResponse

    @FormUrlEncoded
    @PUT("order")
    suspend fun updateStatus(
        @Query("csId") csId: Int,
        @Query("orId") orId: Int,
        @Field("or_status") orStatus: Int
    ): OrdersResponse
}