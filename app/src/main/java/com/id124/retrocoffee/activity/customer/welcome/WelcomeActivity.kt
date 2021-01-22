package com.id124.retrocoffee.activity.customer.welcome

import android.content.Intent
import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.login.LoginActivity
import com.id124.retrocoffee.activity.customer.register.RegisterActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityWelcomeBinding

class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_welcome
        super.onCreate(savedInstanceState)

        bind.btCreateNewAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        bind.btGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}