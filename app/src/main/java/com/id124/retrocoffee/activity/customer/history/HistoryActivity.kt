package com.id124.retrocoffee.activity.customer.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.history.Adapter.HistoryAdapter
import com.id124.retrocoffee.activity.customer.history_detail.HistoryDetailActivity
import com.id124.retrocoffee.activity.customer.product_detail.ProductDetailActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityHistoryBinding
import com.id124.retrocoffee.model.history.HistoryModel
import com.id124.retrocoffee.model.order.OrderModel
import com.id124.retrocoffee.util.Utils

class HistoryActivity : BaseActivity<ActivityHistoryBinding>(), HistoryAdapter.onListOrderClickListener {
    private lateinit var viewModel: HistoryViewModel
    private var listOrder = ArrayList<OrderModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_history
        super.onCreate(savedInstanceState)

        setRecycleView()
        setToolbarActionBar()
        setViewModel()
        subscribeLiveData()
    }

    private fun setRecycleView() {
        val offsetPx = resources.getDimension(R.dimen.bottom_end_recyclerview_home)
        val bottomOffsetDecoration = Utils.Companion.BottomOffsetDecoration(offsetPx.toInt())

        bind.rvHistory.addItemDecoration(bottomOffsetDecoration)
        bind.rvHistory.setHasFixedSize(true)
        bind.rvHistory.adapter = HistoryAdapter(listOrder,this)
        bind.rvHistory.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)

    }

    private fun setToolbarActionBar() {
        setSupportActionBar(bind.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Order History"
        bind.toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@HistoryActivity).get(HistoryViewModel::class.java)
        viewModel.setService(createApi(this@HistoryActivity))
        viewModel.serviceApi(sharedPref.getCsId())
    }

    private fun subscribeLiveData() {
        viewModel.isLoadingLiveData.observe(this@HistoryActivity, {
            if (it) {
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
            }
        })

        viewModel.getAllOrder().observe(this, {
            (bind.rvHistory.adapter as HistoryAdapter).addList(it)
        })
    }

    override fun onOrderItem(position: Int) {
        val intent = Intent(this, HistoryDetailActivity::class.java)
        intent.putExtra("orderId", listOrder[position].orId)
        intent.putExtra("payTotal", listOrder[position].orTotal)
        intent.putExtra("approveNote", listOrder[position].orNoteApprove)
        startActivity(intent)
    }

}