package com.id124.retrocoffee.model.Coupons

import com.id124.retrocoffee.model.category.CategoryModel

class CouponsResponse (
    val success: Boolean,
    val message: String,
    val data: List<CouponModel>
)