package com.id124.retrocoffee.activity.customer.register

import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_register
        super.onCreate(savedInstanceState)
    }
}