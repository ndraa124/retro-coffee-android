package com.id124.retrocoffee.service

import com.id124.retrocoffee.model.account.*
import retrofit2.http.*

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

    @FormUrlEncoded
    @POST("account/email")
    suspend fun cekEmail(
        @Field("email") email: String
    ): VerifyResponse

    @FormUrlEncoded
    @POST("account/password")
    suspend fun checkPassword(
        @Field("acId") acId: Int,
        @Field("password") password : String
    ): PasswordResponse

    @FormUrlEncoded
    @PUT("account/{id}")
    suspend fun updateAccount(
        @Path("id") accountId: Int,
        @Field("ac_name") accountName: String,
        @Field("ac_email") accountEmail: String,
        @Field("ac_phone") accountPhone: String
    ): RegisterResponse

    @FormUrlEncoded
    @PUT("account/password/{acId}")
    suspend fun resetPassword(
        @Path("acId") acId: Int,
        @Field("ac_password") acPassword : String
    ): PasswordResponse
}