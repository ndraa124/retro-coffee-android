package com.id124.retrocoffee.activity.customer.checkout

import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityCheckoutBinding

class CheckoutActivity : BaseActivity<ActivityCheckoutBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_checkout
        super.onCreate(savedInstanceState)
    }
}