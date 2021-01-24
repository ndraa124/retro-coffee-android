package com.id124.retrocoffee.activity.customer.product_detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.cart.CartActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityProductDetailBinding
import com.id124.retrocoffee.model.product.ProductModel
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE
import com.id124.retrocoffee.util.Utils.Companion.currencyFormat

class ProductDetailActivity : BaseActivity<ActivityProductDetailBinding>(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_product_detail
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setDataFromIntent()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_to_cart -> {

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_product, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_cart -> {
                intents<CartActivity>(this@ProductDetailActivity)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setToolbarActionBar() {
        setSupportActionBar(bind.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        bind.toolbar.setNavigationIcon(R.drawable.ic_chevron_left)
        bind.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setDataFromIntent() {
        bind.product = ProductModel(
            ctId = intent.getIntExtra("ct_id", 0),
            ctName = intent.getStringExtra("ct_name")!!,
            prId = intent.getIntExtra("pr_id", 0),
            prName = intent.getStringExtra("pr_name")!!,
            prPrice = intent.getLongExtra("pr_price", 0),
            prDesc = intent.getStringExtra("pr_desc")!!,
            prDiscount = intent.getIntExtra("pr_discount", 0),
            prDiscountPrice = intent.getLongExtra("pr_discount_price", 0),
            prIsDiscount = intent.getIntExtra("pr_is_discount", 0),
            prStatus = intent.getIntExtra("pr_status", 0),
            prPicImage = intent.getStringExtra("pr_pic_image")
        )

        bind.price = currencyFormat(intent.getLongExtra("pr_price", 0).toString())

        if (intent.getStringExtra("pr_pic_image") != null) {
            bind.imageUrl = BASE_URL_IMAGE + intent.getStringExtra("pr_pic_image")
        }
    }
}