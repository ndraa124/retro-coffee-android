package com.id124.retrocoffee.service

import com.id124.retrocoffee.model.favorite.FavoriteResponse
import retrofit2.http.*

interface FavoriteApiService {

    @FormUrlEncoded
    @POST("favorite")
    suspend fun addFavorite(
        @Field("cs_id") csId: Int,
        @Field("pr_id") prId: Int
    ): FavoriteResponse

    @GET("favorite/check")
    suspend fun checkIsFavorite(
        @Query("csId") csId: Int,
        @Query("prId") prId: Int
    ): FavoriteResponse

    @DELETE("favorite")
    suspend fun deleteFavoriteByProduct(
        @Query("cs_id") csId: Int,
        @Query("pr_id") prId: Int
    ): FavoriteResponse

    @GET("favorite/{csId}")
    suspend fun getFavoriteProduct(@Path("csId") costumerID: Int) : FavoriteResponse

    @DELETE("favorite/{faId}")
    suspend fun deleteFavorite(@Path("faId") favoriteID: Int) : FavoriteResponse

}