package com.id124.retrocoffee.activity.customer.forgot_password.new_password

import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityNewPasswordBinding

class NewPasswordActivity : BaseActivity<ActivityNewPasswordBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_new_password
        super.onCreate(savedInstanceState)
    }
}