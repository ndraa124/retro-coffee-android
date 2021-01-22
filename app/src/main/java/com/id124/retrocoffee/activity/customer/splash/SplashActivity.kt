package com.id124.retrocoffee.activity.customer.splash

import android.os.Bundle
import android.os.Handler
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>(), SplashContract.View {

    private lateinit var handler: Handler
    private var presenter: SplashPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_splash
        super.onCreate(savedInstanceState)

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
        TODO("Not yet implemented")
    }

    override fun setError(error: String) {
        TODO("Not yet implemented")
    }
}