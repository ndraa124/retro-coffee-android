package com.id124.retrocoffee.activity.customer.coupon

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.cart.CartActivity
import com.id124.retrocoffee.activity.customer.coupon.adapter.CouponAdapter
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityCouponBinding
import com.id124.retrocoffee.model.coupon.CouponModel

class CouponActivity : BaseActivity<ActivityCouponBinding>(), View.OnClickListener {
    private lateinit var adapter: CouponAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_coupon
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setCouponRecyclerView()
        setDataCoupons()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                intents<CartActivity>(this@CouponActivity)
                this@CouponActivity.finish()
            }
        }
    }

    override fun onBackPressed() {
        intents<CartActivity>(this@CouponActivity)
        this@CouponActivity.finish()
    }

    private fun setToolbarActionBar() {
        setStatusBar()
        setSupportActionBar(bind.toolbar)
    }

    private fun setStatusBar() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = this.resources.getColor(R.color.background, theme)
    }

    private fun setCouponRecyclerView() {
        layoutManager = LinearLayoutManager(this@CouponActivity, RecyclerView.VERTICAL, false)
        bind.rvCoupon.layoutManager = layoutManager

        adapter = CouponAdapter()
        bind.rvCoupon.adapter = adapter

        adapter.setOnItemClickCallback(object : CouponAdapter.OnItemClickCallback {
            override fun onItemClick(data: CouponModel) {
                val intent = Intent(this@CouponActivity, CartActivity::class.java)
                intent.putExtra("is_discount", 1)
                intent.putExtra("cp_price_discount", data.cpPriceDiscount)
                startActivity(intent)
                this@CouponActivity.finish()
            }
        })
    }

    private fun setDataCoupons() {
        val list = arrayListOf<CouponModel>()

        list.add(
            CouponModel(
                cpId = 1,
                cpName = "Get a cup of coffee for free on sunday morning",
                cpPriceDiscount = 10000,
                cpDesc = "Only at 7 to 9 AM",
                cpPicImage = R.drawable.img_coupon_1,
                cpColor = R.color.secondary
            )
        )
        list.add(
            CouponModel(
                cpId = 2,
                cpName = "HAPPY MOTHER’S DAY!",
                cpPriceDiscount = 12000,
                cpDesc = "Get one of our favorite menu for free!",
                cpPicImage = R.drawable.img_coupon_2,
                cpColor = R.color.green_200
            )
        )
        list.add(
            CouponModel(
                cpId = 3,
                cpName = "HAPPY HALLOWEEN!",
                cpPriceDiscount = 17500,
                cpDesc = "Do you like chicken wings? Get 1 free only if you buy pinky promise",
                cpPicImage = R.drawable.img_coupon_3,
                cpColor = R.color.brown_200
            )
        )

        adapter.addList(list)
    }
}