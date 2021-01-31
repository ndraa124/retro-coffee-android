package com.id124.retrocoffee.activity.customer.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.cart.adapter.CartAdapter
import com.id124.retrocoffee.activity.customer.checkout.CheckoutActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityCartBinding
import com.id124.retrocoffee.model.cart.CartModel
import com.id124.retrocoffee.util.Utils

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
        layoutManager = LinearLayoutManager(this@CartActivity, RecyclerView.VERTICAL, false)
        bind.rvCart.layoutManager = layoutManager

        adapter = CartAdapter()
        bind.rvCart.adapter = adapter

        adapter.setOnItemClickCallback(object : CartAdapter.OnItemClickCallback {
            override fun onClickAddItem(data: CartModel) {
                Log.d("msg", "Add Item ${data.crId}")
            }

            override fun onClickRemoveItem(data: CartModel) {
                Log.d("msg", "Remove Item ${data.crId}")
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
        viewModel.isLoading.observe(this) {
            if (it) {
                bind.lnProgressBar.visibility = View.VISIBLE
            } else {
                bind.lnProgressBar.visibility = View.GONE
            }
        }

        viewModel.onSuccess.observe(this) { list ->
            for (i in list.indices) {
                subtotal += list[i].crTotal
            }

            adapter.addList(list)
            bind.lnDataNotFound.visibility = View.GONE
            bind.svCart.visibility = View.VISIBLE

            setPayTotal()
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
}