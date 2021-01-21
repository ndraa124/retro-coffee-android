package com.id124.retrocoffee.activity.customer.welcome

import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityWelcomeBinding

class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_welcome
        super.onCreate(savedInstanceState)
    }
}