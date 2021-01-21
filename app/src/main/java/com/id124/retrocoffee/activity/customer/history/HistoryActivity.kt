package com.id124.retrocoffee.activity.customer.history

import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityHistoryBinding

class HistoryActivity : BaseActivity<ActivityHistoryBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_history
        super.onCreate(savedInstanceState)
    }
}