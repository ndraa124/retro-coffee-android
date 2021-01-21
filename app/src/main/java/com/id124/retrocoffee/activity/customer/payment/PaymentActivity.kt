package com.id124.retrocoffee.activity.customer.payment

import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityPaymentBinding

class PaymentActivity : BaseActivity<ActivityPaymentBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_payment
        super.onCreate(savedInstanceState)
    }
}