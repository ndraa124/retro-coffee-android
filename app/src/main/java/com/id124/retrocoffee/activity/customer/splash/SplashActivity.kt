package com.id124.retrocoffee.activity.customer.splash

import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_splash
        super.onCreate(savedInstanceState)
    }
}