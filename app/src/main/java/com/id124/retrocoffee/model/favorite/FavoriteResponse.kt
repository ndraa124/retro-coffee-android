package com.id124.retrocoffee.model.favorite

data class FavoriteResponse(
    val success: Boolean,
    val message: String,
    val data: List<FavoriteModel>
)