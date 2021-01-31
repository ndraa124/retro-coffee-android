package com.id124.retrocoffee.service

import com.id124.retrocoffee.model.customer.CustomerResponse
import com.id124.retrocoffee.model.customer.CustomerUpdateResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface CustomerApiService {
    @GET("customer/{csId}")
    suspend fun getCustomerByCsId(
        @Path("csId") csId: Int,
    ): CustomerResponse

    @Multipart
    @PUT("customer/{csId}")
    suspend fun updateCustomer(
        @Path("csId") csId: Int,
        @Part("cs_gender") csGender: RequestBody,
        @Part("cs_dob") csDob: RequestBody,
        @Part("cs_address") csAddress: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): CustomerUpdateResponse
}