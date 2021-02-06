package com.id124.retrocoffee.activity.customer.splash

import android.os.Bundle
import android.view.KeyEvent
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.main.MainActivity
import com.id124.retrocoffee.activity.customer.onboard.OnboardActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivitySplashBinding
import com.id124.retrocoffee.util.Permission
import java.util.*

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private var splashTimer: Timer? = null
    private var permissionHelper: Permission? = null
    private var applicationPaused = false
    private var splashTimerHandler: SplashTimerHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_splash
        super.onCreate(savedInstanceState)

        permissionHelper = Permission(this@SplashActivity)
        checkAndRequestPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper?.onRequestCallBack(requestCode, permissions, grantResults)
    }

    private fun checkAndRequestPermissions(): Boolean {
        permissionHelper?.permissionListener(object : Permission.PermissionListener {
            override fun onPermissionCheckDone() {
                setSplash()
            }
        })

        permissionHelper?.checkAndRequestPermissions()

        return true
    }

    private fun setSplash() {
        splashTimerHandler = SplashTimerHandler()
        splashTimer = Timer()
        splashTimer!!.schedule(this.splashTimerHandler, 0, 2000)
    }

    override fun onPause() {
        super.onPause()
        applicationPaused = true
        this.closeSplashTimer()
    }

    override fun onResume() {
        super.onResume()
        if (applicationPaused) {
            applicationPaused = false

            closeSplashTimer()
            setSplash()
        }
    }

    inner class SplashTimerHandler : TimerTask() {
        private var splashTimerCounter = 0

        override fun run() {
            splashTimerCounter++

            if (splashTimerCounter > 2) {
                runOnUiThread(splashTimeOver)
            }
        }

        private val splashTimeOver = Runnable {
            closeSplashTimer()
            startHomeScreen()
        }
    }

    private fun closeSplashTimer() {
        if (splashTimer != null) {
            splashTimer!!.cancel()
            splashTimer = null
        }
    }

    private fun startHomeScreen() {
        closeSplashScreen()

        if (!sharedPref.getIsLogin()) {
            intents<OnboardActivity>(this@SplashActivity)
            this@SplashActivity.finish()
        } else {
            intents<MainActivity>(this@SplashActivity)
            this@SplashActivity.finish()
        }
    }

    private fun closeSplashScreen() {
        closeSplashTimer()
    }

    override fun onKeyDown(keycode: Int, event: KeyEvent?): Boolean {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            closeSplashScreen()
            finish()
        }

        return true
    }
}