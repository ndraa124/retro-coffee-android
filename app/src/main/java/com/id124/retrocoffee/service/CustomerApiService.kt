package com.id124.retrocoffee.service

import com.id124.retrocoffee.model.customer.CustomerResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CustomerApiService {
    @GET("customer/{csId}")
    suspend fun getCustomerByCsId(
        @Path("csId") csId: Int,
    ): CustomerResponse
}