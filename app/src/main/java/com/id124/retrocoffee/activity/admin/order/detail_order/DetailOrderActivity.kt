package com.id124.retrocoffee.activity.admin.order.detail_order

import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityDetailOrderBinding

class DetailOrderActivity : BaseActivity<ActivityDetailOrderBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_detail_order
        super.onCreate(savedInstanceState)
    }
}