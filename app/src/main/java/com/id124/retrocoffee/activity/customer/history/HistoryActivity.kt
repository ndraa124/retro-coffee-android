package com.id124.retrocoffee.activity.customer.history

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.history.Adapter.HistoryAdapter
import com.id124.retrocoffee.activity.customer.main.MainViewModel
import com.id124.retrocoffee.activity.customer.main.fragment.ProductFragment
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityHistoryBinding
import com.id124.retrocoffee.model.history.HistoryModel
import com.id124.retrocoffee.util.ViewPagerAdapter

class HistoryActivity : BaseActivity<ActivityHistoryBinding>(), HistoryAdapter.onListHistoryClickListener {
    private lateinit var viewModel: HistoryViewModel
    private var listHistory = ArrayList<HistoryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_history
        super.onCreate(savedInstanceState)

        bind.rvHistory.adapter = HistoryAdapter(listHistory,this)
        bind.rvHistory.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)

        setToolbarActionBar()
        setViewModel()
        subscribeLiveData()
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
        viewModel.serviceApi(1)
    }

    private fun subscribeLiveData() {
        viewModel.isLoadingLiveData.observe(this@HistoryActivity, {
            if (it) {
                Log.d("msg", "Show Loading")
            } else {
                Log.d("msg", "Hide Loading")
            }
        })

        viewModel.getAllHistory().observe(this, {
            (bind.rvHistory.adapter as HistoryAdapter).addList(it)
        })
    }

    override fun onHistoryItem(position: Int) {
        Toast.makeText(this, "tes", Toast.LENGTH_SHORT).show()
    }

}