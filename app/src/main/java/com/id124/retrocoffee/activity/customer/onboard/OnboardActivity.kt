package com.id124.retrocoffee.activity.customer.onboard

import android.content.Intent
import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.welcome.WelcomeActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityOnboardBinding

class OnboardActivity : BaseActivity<ActivityOnboardBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_onboard
        super.onCreate(savedInstanceState)

        bind.btGetStarted.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }
}