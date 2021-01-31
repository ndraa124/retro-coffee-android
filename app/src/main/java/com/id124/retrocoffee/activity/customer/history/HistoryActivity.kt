package com.id124.retrocoffee.activity.customer.history

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.history.adapter.HistoryAdapter
import com.id124.retrocoffee.activity.customer.history_detail.HistoryDetailActivity
import com.id124.retrocoffee.activity.customer.main.MainActivity
import com.id124.retrocoffee.activity.customer.product_search.ProductSearchActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityHistoryBinding
import com.id124.retrocoffee.model.order.OrderModel
import com.id124.retrocoffee.util.Utils

class HistoryActivity : BaseActivity<ActivityHistoryBinding>(), HistoryAdapter.onListOrderClickListener, View.OnClickListener {
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_start_ordering -> {
                intents<ProductSearchActivity>(this@HistoryActivity)
                this@HistoryActivity.finish()
            }
            R.id.btn_back -> {
                if (intent.getIntExtra("orderTransaction", 0) == 1) {
                    intents<MainActivity>(this@HistoryActivity)
                    this@HistoryActivity.finish()
                } else {
                    onBackPressed()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (intent.getIntExtra("orderTransaction", 0) == 1) {
            intents<MainActivity>(this@HistoryActivity)
            this@HistoryActivity.finish()
        } else {
            super.onBackPressed()
        }
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

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@HistoryActivity).get(HistoryViewModel::class.java)
        viewModel.setService(createApi(this@HistoryActivity))
        viewModel.serviceApi(sharedPref.getCsId())
    }

    private fun subscribeLiveData() {
        viewModel.isLoadingLiveData.observe(this@HistoryActivity) {
            if (it) {
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
            }
        }

        viewModel.onSuccessLiveData.observe(this, { list ->
            bind.lnDataNotFound.visibility = View.GONE
            bind.rvHistory.visibility = View.VISIBLE
            (bind.rvHistory.adapter as HistoryAdapter).addList(list)
        })

        viewModel.onFailLiveData.observe(this, {
            bind.rvHistory.visibility = View.GONE
            bind.lnDataNotFound.visibility = View.VISIBLE
        })
    }

    override fun onOrderItem(position: Int) {
        val intent = Intent(this, HistoryDetailActivity::class.java)
        intent.putExtra("orderId", listOrder[position].orId)
        intent.putExtra("orderAddress", listOrder[position].orAddress)
        intent.putExtra("payTotal", listOrder[position].orTotal)
        intent.putExtra("approveNote", listOrder[position].orNoteApprove)
        startActivity(intent)
    }

}