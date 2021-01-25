package com.id124.retrocoffee.activity.customer.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityFavoriteBinding
import com.id124.retrocoffee.databinding.ActivityOnboardBinding

class FavoriteActivity :  BaseActivity<ActivityFavoriteBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_favorite
        super.onCreate(savedInstanceState)


    }
}