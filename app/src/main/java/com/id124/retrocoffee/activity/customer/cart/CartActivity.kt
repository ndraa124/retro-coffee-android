package com.id124.retrocoffee.activity.customer.cart

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.cart.adapter.CartAdapter
import com.id124.retrocoffee.activity.customer.checkout.CheckoutActivity
import com.id124.retrocoffee.activity.customer.coupon.CouponActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityCartBinding
import com.id124.retrocoffee.model.cart.CartModel
import com.id124.retrocoffee.util.Utils
import java.util.*

class CartActivity : BaseActivity<ActivityCartBinding>(), View.OnClickListener {
    private lateinit var viewModel: CartViewModel
    private lateinit var adapter: CartAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var subtotal: Long = 0
    private var total: Long = 0
    private var fee: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_cart
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setCartRecyclerView()
        setViewModel()
        subscribeLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_checkout -> {
                if (subtotal == 0L) {
                    noticeToast("Please add some product first!")
                } else {
                    intents<CheckoutActivity>(this@CartActivity)
                }
            }
            R.id.btn_see_product -> {
                this@CartActivity.finish()
            }
            R.id.btn_coupons -> {
                intents<CouponActivity>(this@CartActivity)
            }
            R.id.btn_back -> {
                onBackPressed()
            }
        }
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

    private fun setCartRecyclerView() {
        bind.rvCart.isNestedScrollingEnabled = false
        layoutManager = LinearLayoutManager(this@CartActivity, RecyclerView.VERTICAL, false)
        bind.rvCart.layoutManager = layoutManager

        adapter = CartAdapter()
        bind.rvCart.adapter = adapter

        adapter.setOnItemClickCallback(object : CartAdapter.OnItemClickCallback {
            override fun onClickAddItem(data: CartModel) {
                viewModel.serviceUpdateApi(
                    crId = data.crId,
                    crQty = data.crQty + 1
                )
            }

            override fun onClickRemoveItem(data: CartModel) {
                if (data.crQty > 1) {
                    viewModel.serviceUpdateApi(
                        crId = data.crId,
                        crQty = data.crQty - 1
                    )
                } else {
                    confirmDelete(crId = data.crId)
                }
            }
        })
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        viewModel.setService(createApi(this))
        viewModel.serviceGetApi(
            csId = sharedPref.getCsId()
        )
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this@CartActivity) {
            if (it) {
                bind.lnProgressBar.visibility = View.VISIBLE
            } else {
                bind.lnProgressBar.visibility = View.GONE
            }
        }

        viewModel.onSuccess.observe(this@CartActivity) { list ->
            for (i in list.indices) {
                subtotal += list[i].crTotal
            }

            adapter.addList(list)
            bind.lnDataNotFound.visibility = View.GONE
            bind.svCart.visibility = View.VISIBLE

            setPayTotal()
        }

        viewModel.onSuccessCart.observe(this@CartActivity) {
            if (it) {
                intents<CartActivity>(this@CartActivity)
                finish()
            }
        }

        viewModel.onFail.observe(this@CartActivity) {
            bind.svCart.visibility = View.GONE
            bind.lnDataNotFound.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPayTotal() {
        fee = 5000
        total = subtotal + fee

        bind.tvIdrTotal.text = Utils.currencyFormat(subtotal.toString())
        bind.tvTaxTotal.text = Utils.currencyFormat(fee.toString())
        bind.tvPayTotal.text = Utils.currencyFormat(total.toString())
    }

    private fun confirmDelete(crId: Int) {
        val dialog = AlertDialog
            .Builder(this@CartActivity)
            .setTitle("Notice!")
            .setMessage("Are you sure to remove this product?")
            .setPositiveButton("OK") { _, _ ->
                viewModel.serviceDeleteApi(
                    crId = crId
                )
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        dialog?.show()
    }
}