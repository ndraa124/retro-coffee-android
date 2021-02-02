package com.id124.retrocoffee.activity.customer.coupon

import CouponAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.id124.retrocoffee.R
import kotlinx.android.synthetic.main.activity_coupon.*

class CouponActivity : AppCompatActivity() {
    private var dataImage = arrayOf(R.drawable.couponbg)
    private var dataTitle = arrayOf("kupon satu")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon)

        val adapter = CouponAdapter(dataImage,dataTitle)
        couponlist.adapter = adapter

        couponlist.layoutManager = StaggeredGridLayoutManager(1,0)
    }
}