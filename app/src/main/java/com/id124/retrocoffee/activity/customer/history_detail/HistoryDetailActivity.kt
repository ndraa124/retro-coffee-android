package com.id124.retrocoffee.activity.customer.history_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.history.Adapter.HistoryAdapter
import com.id124.retrocoffee.activity.customer.history.HistoryViewModel
import com.id124.retrocoffee.activity.customer.history_detail.adapter.HistoryDetailAdapter
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityHistoryDetailBinding
import com.id124.retrocoffee.model.cart.CartModel
import com.id124.retrocoffee.model.history.HistoryModel
import com.id124.retrocoffee.util.Utils

class HistoryDetailActivity : BaseActivity<ActivityHistoryDetailBinding>(), View.OnClickListener {
    private lateinit var viewModel: HistoryDetailViewModel
    private var listHistory = ArrayList<HistoryModel>()

    private var subtotal: Long = 0
    private var total: Long = 0
    private var fee: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_history_detail
        super.onCreate(savedInstanceState)


        setRecycleView()
        setViewModel()
        subscribeLiveData()
        setPayTotal()
        setButtonChooseStore()
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

    private fun setButtonChooseStore() {
        var approveNote = intent.getStringExtra("approveNote")
        if(approveNote == "Dine In") {
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

        else if(approveNote == "Door Delivery") {
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

        else if(approveNote == "Pick Up") {
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

    private fun setPayTotal() {
        fee = 5000

        bind.tvIdrTotal.text = Utils.currencyFormat(subtotal.toString())
        bind.tvTaxTotal.text = Utils.currencyFormat(fee.toString())
        bind.tvPayTotal.text = intent.getStringExtra("payTotal")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                onBackPressed()
            }
        }
    }
}