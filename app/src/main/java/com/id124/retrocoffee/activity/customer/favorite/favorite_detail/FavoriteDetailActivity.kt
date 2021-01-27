package com.id124.retrocoffee.activity.customer.favorite.favorite_detail

import android.os.Bundle
import com.bumptech.glide.Glide
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.favorite.FavoritePresenter
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityFavoriteDetailBinding
import kotlinx.coroutines.CoroutineScope

class FavoriteDetailActivity : BaseActivity<ActivityFavoriteDetailBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_favorite_detail
        super.onCreate(savedInstanceState)

        // Set Intent Data
        bind.tvProductName.text = intent.getStringExtra("productName")
        bind.tvProductDesc.text = intent.getStringExtra("productDesc")
        Glide.with(this)
            .load("http://18.212.11.190:3000/images/${intent.getStringExtra("productImage")}")
            .into(bind.ivProductImage)

        //
    }

}