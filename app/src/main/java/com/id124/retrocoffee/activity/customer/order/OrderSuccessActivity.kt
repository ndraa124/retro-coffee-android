package com.id124.retrocoffee.activity.customer.order

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.history.HistoryActivity
import com.id124.retrocoffee.activity.customer.main.MainActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityOrderSuccessBinding

class OrderSuccessActivity : BaseActivity<ActivityOrderSuccessBinding>(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_order_success
        super.onCreate(savedInstanceState)

        setStatusBar()
        setDataFromIntent()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_success -> {
                val intent = Intent(this@OrderSuccessActivity, HistoryActivity::class.java)
                intent.putExtra("orderTransaction", 1)
                startActivity(intent)
                this@OrderSuccessActivity.finish()
            }
        }
    }

    override fun onBackPressed() {
        intents<MainActivity>(this@OrderSuccessActivity)
        this@OrderSuccessActivity.finish()
    }

    private fun setStatusBar() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.green, theme)
    }

    private fun setDataFromIntent() {
        bind.dateOrder = "Date order: ${intent.getStringExtra("date_order")}"
    }
}