package com.id124.retrocoffee.activity.customer.checkout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.cart.CartActivity
import com.id124.retrocoffee.activity.customer.checkout.adapter.CartAdapter
import com.id124.retrocoffee.activity.customer.payment.PaymentActivity
import com.id124.retrocoffee.activity.customer.profile.EditProfileActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityCheckoutBinding
import com.id124.retrocoffee.util.Utils.Companion.currencyFormat

class CheckoutActivity : BaseActivity<ActivityCheckoutBinding>(), View.OnClickListener {
    private lateinit var viewModel: CheckoutViewModel
    private lateinit var adapter: CartAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var chooseStore: String? = null
    private var subtotal: Long = 0
    private var total: Long = 0
    private var fee: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_checkout
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setTouchSelected()
        setProductRecyclerView()
        setViewModel()
        subscribeLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_choose_address -> {
                val intent = Intent(this@CheckoutActivity, EditProfileActivity::class.java)
                intent.putExtra("checkout", 1)
                startActivity(intent)
            }
            R.id.btn_payment -> {
                when {
                    sharedPref.getCsAddress() == null -> {
                        noticeToast("Please add address first!")
                        val intent = Intent(this@CheckoutActivity, EditProfileActivity::class.java)
                        intent.putExtra("checkout", 1)
                        startActivity(intent)
                    }
                    chooseStore == null -> {
                        noticeToast("Please choose store first!")
                    }
                    else -> {
                        val intent = Intent(this@CheckoutActivity, PaymentActivity::class.java)
                        intent.putExtra("pay_total", total)
                        intent.putExtra("store", chooseStore)
                        startActivity(intent)
                        this@CheckoutActivity.finish()
                    }
                }
            }
            R.id.btn_back -> {
                intents<CartActivity>(this@CheckoutActivity)
                this@CheckoutActivity.finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (sharedPref.getCsAddress() == "") {
            bind.address = "Empty address! Please add address first!"
            bind.addressDelivery.setTextColor(resources.getColor(R.color.gray_500, theme))
        } else {
            bind.address = sharedPref.getCsAddress()
        }
    }

    override fun onResume() {
        super.onResume()
        setTouchSelectedResume()

        if (sharedPref.getCsAddress() == "") {
            bind.address = "Empty address! Please add address first!"
            bind.addressDelivery.setTextColor(resources.getColor(R.color.gray_500, theme))
        } else {
            bind.address = sharedPref.getCsAddress()
        }
    }

    override fun onBackPressed() {
        intents<CartActivity>(this@CheckoutActivity)
        this@CheckoutActivity.finish()
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchSelected() {
        bind.btnDineIn.setOnTouchListener { _, _ ->
            chooseStore = "Dine In"

            bind.btnDineIn.setTextColor(resources.getColor(R.color.white, theme))
            bind.btnDoorDelivery.setTextColor(resources.getColor(R.color.gray_500, theme))
            bind.btnPickUp.setTextColor(resources.getColor(R.color.gray_500, theme))

            bind.btnDineIn.isPressed = true
            bind.btnDoorDelivery.isPressed = false
            bind.btnPickUp.isPressed = false
            true
        }

        bind.btnDoorDelivery.setOnTouchListener { _, _ ->
            chooseStore = "Door Delivery"

            bind.btnDineIn.setTextColor(resources.getColor(R.color.gray_500, theme))
            bind.btnDoorDelivery.setTextColor(resources.getColor(R.color.white, theme))
            bind.btnPickUp.setTextColor(resources.getColor(R.color.gray_500, theme))

            bind.btnDineIn.isPressed = false
            bind.btnDoorDelivery.isPressed = true
            bind.btnPickUp.isPressed = false
            true
        }

        bind.btnPickUp.setOnTouchListener { _, _ ->
            chooseStore = "Pick Up"

            bind.btnDineIn.setTextColor(resources.getColor(R.color.gray_500, theme))
            bind.btnDoorDelivery.setTextColor(resources.getColor(R.color.gray_500, theme))
            bind.btnPickUp.setTextColor(resources.getColor(R.color.white, theme))

            bind.btnDineIn.isPressed = false
            bind.btnDoorDelivery.isPressed = false
            bind.btnPickUp.isPressed = true
            true
        }
    }

    private fun setTouchSelectedResume() {
        when (chooseStore) {
            "Dine In" -> {
                bind.btnDineIn.setTextColor(resources.getColor(R.color.white, theme))
                bind.btnDoorDelivery.setTextColor(resources.getColor(R.color.gray_500, theme))
                bind.btnPickUp.setTextColor(resources.getColor(R.color.gray_500, theme))

                bind.btnDineIn.isPressed = true
                bind.btnDoorDelivery.isPressed = false
                bind.btnPickUp.isPressed = false
            }
            "Door Delivery" -> {
                bind.btnDineIn.setTextColor(resources.getColor(R.color.gray_500, theme))
                bind.btnDoorDelivery.setTextColor(resources.getColor(R.color.white, theme))
                bind.btnPickUp.setTextColor(resources.getColor(R.color.gray_500, theme))

                bind.btnDineIn.isPressed = false
                bind.btnDoorDelivery.isPressed = true
                bind.btnPickUp.isPressed = false
            }
            "Pick Up" -> {
                bind.btnDineIn.setTextColor(resources.getColor(R.color.gray_500, theme))
                bind.btnDoorDelivery.setTextColor(resources.getColor(R.color.gray_500, theme))
                bind.btnPickUp.setTextColor(resources.getColor(R.color.white, theme))

                bind.btnDineIn.isPressed = false
                bind.btnDoorDelivery.isPressed = false
                bind.btnPickUp.isPressed = true
            }
        }
    }

    private fun setProductRecyclerView() {
        layoutManager = LinearLayoutManager(this@CheckoutActivity, RecyclerView.VERTICAL, false)
        bind.rvProduct.layoutManager = layoutManager

        adapter = CartAdapter()
        bind.rvProduct.adapter = adapter
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@CheckoutActivity).get(CheckoutViewModel::class.java)
        viewModel.setService(createApi(this@CheckoutActivity))
        viewModel.serviceGetApi(
            csId = sharedPref.getCsId()
        )
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this@CheckoutActivity) {
            if (it) {
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
            }
        }

        viewModel.onSuccess.observe(this@CheckoutActivity) { list ->
            for (i in list.indices) {
                subtotal += list[i].crTotal
            }

            adapter.addList(list)
            bind.tvDataNotFound.visibility = View.GONE

            setPayTotal()
        }

        viewModel.onFail.observe(this@CheckoutActivity) { message ->
            bind.tvDataNotFound.text = message
            bind.tvDataNotFound.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPayTotal() {
        fee = 5000
        total = subtotal + fee

        bind.tvIdrTotal.text = currencyFormat(subtotal.toString())
        bind.tvTaxTotal.text = currencyFormat(fee.toString())
        bind.tvPayTotal.text = currencyFormat(total.toString())
    }
}