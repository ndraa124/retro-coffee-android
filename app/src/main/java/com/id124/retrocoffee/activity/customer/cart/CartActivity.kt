package com.id124.retrocoffee.activity.customer.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.cart.adapter.AdapterCart
import com.id124.retrocoffee.activity.customer.cart.adapter.OnRecyclerViewClickListener
import com.id124.retrocoffee.activity.customer.checkout.CheckoutActivity
import com.id124.retrocoffee.activity.customer.checkout.adapter.CartAdapter
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityCartBinding
import com.id124.retrocoffee.model.cart.CartModel
import com.id124.retrocoffee.util.Utils

class CartActivity : BaseActivity<ActivityCartBinding>(), OnRecyclerViewClickListener {
    private lateinit var viewModel: CartViewModel
    private lateinit var adapter: AdapterCart
    private lateinit var layoutManager: LinearLayoutManager
    var listCart = ArrayList<CartModel>()

    private var subtotal: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_cart
        super.onCreate(savedInstanceState)

        bind.toolbar.title = "My Cart"
        setSupportActionBar(bind.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bind.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        bind.btnCheckout.setOnClickListener {
            if (subtotal.toInt() == 0) {
                noticeToast("You dont have any cart")
            } else {
                intents<CheckoutActivity>(this@CartActivity)
            }
        }

        setProductRecyclerView()
        setViewModel()
        subscribeLiveData()
        setPayTotal()

    }

    private fun setProductRecyclerView() {
        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        bind.rvCart.layoutManager = layoutManager

        adapter = AdapterCart(listCart, this)
        bind.rvCart.adapter = adapter
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
                bind.progressBar.visibility = View.VISIBLE
                bind.emptyImage.visibility = View.GONE
                bind.emptyText.visibility = View.GONE
            } else {
                bind.progressBar.visibility = View.GONE
            }
        }

        viewModel.onSuccess.observe(this) { list ->
            subtotal = 0
            for (i in list.indices) {
                subtotal += list[i].crTotal
            }

            adapter.addList(list)
            bind.emptyText.visibility = View.GONE
            bind.emptyImage.visibility = View.GONE
            bind.rvCart.visibility = View.VISIBLE

            setPayTotal()

        }

        viewModel.onFail.observe(this) { message ->
            subtotal = 0

            bind.itemTotal.text = Utils.currencyFormat(subtotal.toString())
            bind.totalPrice.text = Utils.currencyFormat(subtotal.toString())

            bind.emptyText.text = message
            bind.emptyText.visibility = View.VISIBLE
            bind.emptyImage.visibility = View.VISIBLE
            bind.rvCart.visibility = View.GONE
        }

        viewModel.onSuccessCart.observe(this) {
            noticeToast("Success delete cart")
            viewModel.serviceGetApi(csId = sharedPref.getCsId())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPayTotal() {

        bind.itemTotal.text = Utils.currencyFormat(subtotal.toString())
        bind.totalPrice.text = Utils.currencyFormat(subtotal.toString())
    }

    override fun onRecyclerViewItemClicked(position: Int) {
        sharedPref.createCartId(listCart[position].crId)
        val cartId = sharedPref.getCartId()
        Toast.makeText(this, cartId.toString(), Toast.LENGTH_SHORT).show()
        viewModel.serviceDeleteApi(cartId)
        setPayTotal()
    }
}