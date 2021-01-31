package com.id124.retrocoffee.service

import com.id124.retrocoffee.model.account.RegisterResponse
import com.id124.retrocoffee.model.customer.CustomerResponse
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
        @Part("cs_address") csAddress: RequestBody
    ): RegisterResponse

    @Multipart
    @PUT("customer/{csId}")
    suspend fun updateCustomerWithImage(
        @Path("csId") csId: Int,
        @Part("cs_gender") csGender: RequestBody,
        @Part("cs_dob") csDob: RequestBody,
        @Part("cs_address") csAddress: RequestBody,
        @Part image: MultipartBody.Part
    ): RegisterResponse
}