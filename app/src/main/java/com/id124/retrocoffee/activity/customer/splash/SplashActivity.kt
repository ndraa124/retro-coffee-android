package com.id124.retrocoffee.activity.customer.splash

import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_splash
        super.onCreate(savedInstanceState)
<<<<<<< HEAD
<<<<<<< HEAD

        // Set CountDown
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
        //Condition Login
        val condition = 0 // Just Asal nanti diperbaiki ketika login dah selesai

        Handler().postDelayed({
            if(condition == 1){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this, OnboardActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)

=======
>>>>>>> parent of 94aa155... add new onboarding looks
=======
>>>>>>> parent of 94aa155... add new onboarding looks
    }
}