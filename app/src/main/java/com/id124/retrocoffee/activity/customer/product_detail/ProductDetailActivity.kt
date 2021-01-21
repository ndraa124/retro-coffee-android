package com.id124.retrocoffee.activity.customer.product_detail

import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityProductDetailBinding

class ProductDetailActivity : BaseActivity<ActivityProductDetailBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_product_detail
        super.onCreate(savedInstanceState)
    }
}