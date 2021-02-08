package com.id124.retrocoffee.activity.admin.order

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.admin.order.adapter.OrderAdapter
import com.id124.retrocoffee.activity.admin.order.detail_order.DetailOrderActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityOrderBinding
import com.id124.retrocoffee.model.order_admin.OrderModel
import com.id124.retrocoffee.util.Utils

class OrderActivity : BaseActivity<ActivityOrderBinding>(), View.OnClickListener {
    private lateinit var viewModel: OrderViewModel
    private lateinit var adapter: OrderAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_order
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setOrderRecycleView()
        setViewModel()
        subscribeLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.serviceGetApi()
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

    private fun setOrderRecycleView() {
        val offsetPx = resources.getDimension(R.dimen.bottom_end_recyclerview_home)
        val bottomOffsetDecoration = Utils.Companion.BottomOffsetDecoration(offsetPx.toInt())

        bind.rvOrder.addItemDecoration(bottomOffsetDecoration)
        bind.rvOrder.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        bind.rvOrder.layoutManager = layoutManager

        adapter = OrderAdapter()
        bind.rvOrder.adapter = adapter

        adapter.setOnItemClickCallback(object : OrderAdapter.OnItemClickCallback {
            override fun onItemClick(data: OrderModel) {
                val intent = Intent(this@OrderActivity, DetailOrderActivity::class.java)
                intent.putExtra("or_id", data.orId)
                intent.putExtra("cs_id", data.csId)
                intent.putExtra("or_total", data.orPayTotal.toInt())
                intent.putExtra("or_address", data.orAddress)
                intent.putExtra("or_status", data.orStatus)
                intent.putExtra("or_note_cancel", data.orNoteCancel)
                intent.putExtra("or_note_approve", data.orNoteApprove)
                intent.putExtra("or_method_payment", data.orMethodPayment)
                intent.putExtra("or_fee", data.orFee)
                intent.putExtra("or_date_order", data.orDateOrder.split("T")[0])
                startActivity(intent)
            }
        })
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@OrderActivity).get(OrderViewModel::class.java)
        viewModel.setService(createApi(this@OrderActivity))
        viewModel.serviceGetApi()
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this@OrderActivity) {
            if (it) {
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
            }
        }

        viewModel.onSuccess.observe(this@OrderActivity) { list ->
            adapter.addList(list)
            bind.lnDataNotFound.visibility = View.GONE
        }

        viewModel.onFail.observe(this@OrderActivity) {
            bind.lnDataNotFound.visibility = View.VISIBLE
        }
    }
}