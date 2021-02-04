package com.id124.retrocoffee.activity.customer.product_detail

import android.graphics.Paint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.cart.CartActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityProductDetailBinding
import com.id124.retrocoffee.model.product.ProductModel
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE
import com.id124.retrocoffee.util.Utils.Companion.currencyFormat
import kotlin.math.min


class ProductDetailActivity : BaseActivity<ActivityProductDetailBinding>(), View.OnClickListener {
    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var actionMenu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_product_detail
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setDataFromIntent()
        setViewModel()
        subscribeLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_to_cart -> {
                val price: Long
                val total: Long

                if (intent.getIntExtra("pr_is_discount", 0) == 1) {
                    price = intent.getLongExtra("pr_discount_price", 0)
                    total = intent.getLongExtra("pr_discount_price", 0) * 1
                } else {
                    price = intent.getLongExtra("pr_price", 0)
                    total = intent.getLongExtra("pr_price", 0) * 1
                }

                viewModel.serviceAddApi(
                    csId = sharedPref.getCsId(),
                    crProduct = intent.getStringExtra("pr_name")!!,
                    crPrice = price,
                    crQty = 1,
                    crTotal = total,
                    crExpired = 0,
                    crPicImage = intent.getStringExtra("pr_pic_image")!!,
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_product, menu)
        actionMenu = menu!!

        val menuItem = actionMenu.findItem(R.id.nav_cart)
        val actionView: View = menuItem.actionView
        val cartBadge: TextView = actionView.findViewById(R.id.cart_badge_detail)

        if (sharedPref.getAcLevel() == 0) {
            actionMenu.findItem(R.id.nav_favorite).isVisible = false
            actionMenu.findItem(R.id.nav_unfavorite).isVisible = false
            actionMenu.findItem(R.id.nav_cart).isVisible = false
        } else {
            viewModel.onSuccessCheckFavorite.observe(this@ProductDetailActivity) {
                if (it) {
                    actionMenu.findItem(R.id.nav_favorite).isVisible = true
                    actionMenu.findItem(R.id.nav_unfavorite).isVisible = false
                } else {
                    actionMenu.findItem(R.id.nav_favorite).isVisible = false
                    actionMenu.findItem(R.id.nav_unfavorite).isVisible = true
                }
            }
        }

        viewModel.onSuccessFavorite.observe(this@ProductDetailActivity) {
            noticeToast("Success add to favorite")
        }

        subscribeCartLiveData(
            cartView = cartBadge
        )

        actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_unfavorite -> {
                viewModel.serviceAddFavoriteApi(
                    csId = sharedPref.getCsId(),
                    prId = intent.getIntExtra("pr_id", 0)
                )

                actionMenu.findItem(R.id.nav_favorite).isVisible = true
                actionMenu.findItem(R.id.nav_unfavorite).isVisible = false

                return true
            }
            R.id.nav_cart -> {
                intents<CartActivity>(this@ProductDetailActivity)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        viewModel.serviceGetCartApi(
            csId = sharedPref.getCsId()
        )
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

        if (intent.getIntExtra("pr_is_discount", 0) == 1) {
            bind.tvPriceDiscount.paintFlags =
                bind.tvPriceDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            bind.tvPriceDiscount.visibility = View.VISIBLE

            bind.price = currencyFormat(intent.getLongExtra("pr_discount_price", 0).toString())
            bind.priceDiscount = currencyFormat(intent.getLongExtra("pr_price", 0).toString())
        } else {
            bind.tvPriceDiscount.visibility = View.GONE
            bind.price = currencyFormat(intent.getLongExtra("pr_price", 0).toString())
        }

        if (intent.getStringExtra("pr_pic_image") != null) {
            bind.imageUrl = BASE_URL_IMAGE + intent.getStringExtra("pr_pic_image")
        }
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@ProductDetailActivity).get(ProductDetailViewModel::class.java)
        viewModel.setServiceCart(createApi(this@ProductDetailActivity))
        viewModel.setServiceFavorite(createApi(this@ProductDetailActivity))
        viewModel.serviceCheckApi(
            csId = sharedPref.getCsId(),
            prId = intent.getIntExtra("pr_id", 0)
        )
        viewModel.serviceGetCartApi(
            csId = sharedPref.getCsId()
        )
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this@ProductDetailActivity) {
            if (it) {
                bind.btnAddToCart.visibility = View.GONE
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
                bind.btnAddToCart.visibility = View.VISIBLE
            }
        }

        viewModel.onSuccessCart.observe(this@ProductDetailActivity) {
            noticeToast("Success add to cart")

            viewModel.serviceGetCartApi(
                csId = sharedPref.getCsId()
            )
        }
    }

    private fun subscribeCartLiveData(cartView: TextView) {
        viewModel.onSuccessCarts.observe(this@ProductDetailActivity, { list ->
            countBadge(list.size, cartView)
        })

        viewModel.onFailCart.observe(this@ProductDetailActivity, {
            countBadge(0, cartView)
        })
    }

    private fun countBadge(mCartItemCount: Int, cartView: TextView) {
        if (mCartItemCount == 0) {
            if (cartView.visibility != View.GONE) {
                cartView.visibility = View.GONE
            }
        } else {
            cartView.text = min(mCartItemCount, 99).toString()

            if (cartView.visibility != View.VISIBLE) {
                cartView.visibility = View.VISIBLE
            }
        }
    }
}