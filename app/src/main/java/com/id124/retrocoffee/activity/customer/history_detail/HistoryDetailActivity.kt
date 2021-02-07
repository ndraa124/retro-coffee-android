package com.id124.retrocoffee.activity.customer.history_detail

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.history_detail.adapter.HistoryDetailAdapter
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityHistoryDetailBinding
import com.id124.retrocoffee.model.history.HistoryModel
import com.id124.retrocoffee.util.Utils

class HistoryDetailActivity : BaseActivity<ActivityHistoryDetailBinding>(), View.OnClickListener {
    private lateinit var viewModel: HistoryDetailViewModel
    private var listHistory = ArrayList<HistoryModel>()

    private var subtotal: Long = 0
    private var fee: Long = 0
    private var discount: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_history_detail
        super.onCreate(savedInstanceState)

        setRecycleView()
        setToolbarActionBar()
        setViewModel()
        subscribeLiveData()
        setPayTotal()
        setIntentData()
        setView()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
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

    private fun setRecycleView(){
        bind.rvProduct.adapter = HistoryDetailAdapter(listHistory)
        bind.rvProduct.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@HistoryDetailActivity).get(HistoryDetailViewModel::class.java)
        viewModel.setService(createApi(this@HistoryDetailActivity))
        viewModel.serviceApi(intent.getIntExtra("orderId", 0))
    }

    private fun subscribeLiveData() {
        viewModel.isLoadingLiveData.observe(this@HistoryDetailActivity, {
            if (it) {
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
            }
        })

        viewModel.getAllHistory().observe(this, {
            (bind.rvProduct.adapter as HistoryDetailAdapter).addList(it)
        })

        viewModel.listHistory.observe(this@HistoryDetailActivity, { list ->
            for (i in list.indices) {
                subtotal += list[i].htTotal!!
            }

            bind.tvDataNotFound.visibility = View.GONE

            setPayTotal()
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setIntentData() {
        if (intent.getStringExtra("orderAddress") == "") {
            bind.address = "Address is empty!"
        } else {
            bind.address = intent.getStringExtra("orderAddress")
        }

        when (intent.getStringExtra("approveNote")) {
            "Dine In" -> {
                bind.btnDineIn.setTextColor(resources.getColor(R.color.white, theme))
                bind.btnDoorDelivery.setTextColor(resources.getColor(R.color.gray_500, theme))
                bind.btnPickUp.setTextColor(resources.getColor(R.color.gray_500, theme))

                bind.btnDineIn.isPressed = true
                bind.btnDineIn.isClickable = false

                bind.btnDoorDelivery.isPressed = false
                bind.btnDoorDelivery.isClickable = false

                bind.btnPickUp.isPressed = false
                bind.btnPickUp.isClickable = false
            }
            "Door Delivery" -> {
                bind.btnDineIn.setTextColor(resources.getColor(R.color.gray_500, theme))
                bind.btnDoorDelivery.setTextColor(resources.getColor(R.color.white, theme))
                bind.btnPickUp.setTextColor(resources.getColor(R.color.gray_500, theme))

                bind.btnDineIn.isPressed = false
                bind.btnDoorDelivery.isPressed = true
                bind.btnPickUp.isPressed = false

                bind.btnDineIn.isClickable = false
                bind.btnDoorDelivery.isClickable = false
                bind.btnPickUp.isClickable = false
            }
            "Pick Up" -> {
                bind.btnDineIn.setTextColor(resources.getColor(R.color.gray_500, theme))
                bind.btnDoorDelivery.setTextColor(resources.getColor(R.color.gray_500, theme))
                bind.btnPickUp.setTextColor(resources.getColor(R.color.white, theme))


                bind.btnDineIn.isPressed = false
                bind.btnDoorDelivery.isPressed = false
                bind.btnPickUp.isPressed = true

                bind.btnDineIn.isClickable = false
                bind.btnDoorDelivery.isClickable = false
                bind.btnPickUp.isClickable = false
            }
        }
    }

    private fun setView() {
        bind.tvDiscountTotal.visibility = View.GONE
        bind.tvDiscount.visibility = View.GONE
    }

    private fun setPayTotal() {
        fee = 5000

        var subTotal =  Utils.currencyFormat(subtotal.toString())
        bind.tvIdrTotal.text = subTotal

        var tax = Utils.currencyFormat(fee.toString())
        bind.tvTaxTotal.text = tax

        var discount =  intent.getStringExtra("payTotal").toInt() - subtotal
        bind.tvDiscountTotal.text = Utils.currencyFormat(discount.toString())

        if(discount.toInt() == 5000) {
            var payTotal = intent.getStringExtra("payTotal").toInt()
            bind.tvPayTotal.text = Utils.currencyFormat(payTotal.toString())
        } else if (discount.toInt() != 5000) {
            bind.tvDiscountTotal.visibility = View.VISIBLE
            bind.tvDiscount.visibility = View.VISIBLE
            var payTotal = intent.getStringExtra("payTotal").toInt() + fee
            bind.tvPayTotal.text = Utils.currencyFormat(payTotal.toString())
        }

    }
}