package com.id124.retrocoffee.activity.customer.favorite

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.product_search.ProductSearchActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityFavoriteBinding
import com.id124.retrocoffee.model.favorite.FavoriteModel
import com.id124.retrocoffee.remote.ApiClient
import com.id124.retrocoffee.service.FavoriteApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class FavoriteActivity :  BaseActivity<ActivityFavoriteBinding>(), FavoriteContract.View {

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var handler: Handler

    private var presenter: FavoritePresenter? = null
    private var costumerID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_favorite
        super.onCreate(savedInstanceState)

        //Set Service
        setService()

        //Set RecyclerView
        setRecyclerView()

        //Data Refresh Management
        dataRefreshManagement()

        //Show ProgressBar
        showProgressBar()

    }

    override fun addFavoriteList(list: List<FavoriteModel>) {
        (bind.rvFavoriteProduct.adapter as FavoriteAdapter).addList(list)
        bind.rvFavoriteProduct.visibility = View.VISIBLE
        bind.lnNotFound.visibility = View.GONE
    }
    override fun setError(error: String) {
        bind.loadingScreen.visibility = View.GONE
        bind.lnNotFound.visibility = View.VISIBLE
        bind.rvFavoriteProduct.visibility = View.GONE

        bind.btGoSearch.setOnClickListener {
            startActivity(Intent(this, ProductSearchActivity::class.java))
        }
    }

    override fun setService() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = this.let { ApiClient.getApiClient(it).create(FavoriteApiService::class.java) }
        presenter = FavoritePresenter(coroutineScope, service)
    }

    override fun getSavedCostumerID() {
        costumerID = sharedPref.getCsId()
    }

    override fun setRecyclerView() {
        bind.rvFavoriteProduct.adapter = FavoriteAdapter()
        bind.rvFavoriteProduct.layoutManager = GridLayoutManager(
            this,
            2,
            GridLayoutManager.VERTICAL,
            false
        )
    }

    override fun dataRefreshManagement() {
        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                presenter?.getFavorite(costumerID!!)
                handler.postDelayed(this, 2000)
            }
        })
    }

    override fun showProgressBar() {
        bind.loadingScreen.visibility = View.VISIBLE
        bind.progressBar.max = 100
    }

    override fun hideProgressBar() {
        bind.loadingScreen.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter?.unbind()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}