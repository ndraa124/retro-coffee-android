package com.id124.retrocoffee.service

import com.id124.retrocoffee.model.account.LoginResponse
import com.id124.retrocoffee.model.account.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AccountApiService {
    @FormUrlEncoded
    @POST("account/login")
    suspend fun loginAccount(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("account/register")
    suspend fun signUpEngineerAccount(
        @Field("ac_name") acName: String,
        @Field("ac_email") acEmail: String,
        @Field("ac_phone") acPhone: String,
        @Field("ac_password") acPassword: String,
        @Field("ac_level") acLevel: Int,
        @Field("ac_status") acStatus: Int
    ): RegisterResponse
}