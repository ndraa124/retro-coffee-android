package com.id124.retrocoffee.model.category

data class CategoryResponse(
    val success: Boolean,
    val message: String,
    val data: List<CategoryModel>
)