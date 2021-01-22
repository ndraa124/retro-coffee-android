package com.id124.retrocoffee.activity.customer.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.os.HandlerCompat.postDelayed
import androidx.navigation.fragment.findNavController
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.main.MainActivity
import com.id124.retrocoffee.activity.customer.onboard.OnboardActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>(), SplashContract.View {

    private var presenter: SplashPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_splash
        super.onCreate(savedInstanceState)

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
                setError()
                startActivity(intent)
                finish()
            }
        }, 3000)
    }

    override fun setError() {
        
    }
}