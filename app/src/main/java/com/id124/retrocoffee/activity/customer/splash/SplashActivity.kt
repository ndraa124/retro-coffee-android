package com.id124.retrocoffee.activity.customer.splash

import android.os.Bundle
import android.os.Handler
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.cart.CartActivity
import com.id124.retrocoffee.activity.customer.main.MainActivity
import com.id124.retrocoffee.activity.customer.onboard.OnboardActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>(), SplashContract.View {

    private var presenter: SplashPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_splash
        super.onCreate(savedInstanceState)

        setCountdown()
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter?.unbind()
    }

    override fun setCountdown() {
        Handler().postDelayed({
            if(sharedPref.getIsLogin()) {
                intents<CartActivity>(this@SplashActivity)
                finish()
            }
            else {
                intents<CartActivity>(this@SplashActivity)
                finish()
            }
        }, 3000)
    }

    override fun setError() {
    }
}