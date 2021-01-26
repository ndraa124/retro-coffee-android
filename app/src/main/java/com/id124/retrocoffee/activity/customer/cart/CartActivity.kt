package com.id124.retrocoffee.activity.customer.cart

import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.checkout.CheckoutActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityCartBinding

class CartActivity : BaseActivity<ActivityCartBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_cart
        super.onCreate(savedInstanceState)

        bind.toolbar.title = "My Cart"
        setSupportActionBar(bind.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bind.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        bind.btnCheckout.setOnClickListener {
            intents<CheckoutActivity>(this@CartActivity)
        }
    }
}