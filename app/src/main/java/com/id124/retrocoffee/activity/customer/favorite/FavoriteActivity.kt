package com.id124.retrocoffee.activity.customer.favorite

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.id124.retrocoffee.R
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

    private var presenter: FavoritePresenter? = null
    private var costumerID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_favorite
        super.onCreate(savedInstanceState)

        //Set Service
        setService()

        //Set RecyclerView
        setRecyclerView()

        //Set Favorite list
        setFavoriteList()

        //Show ProgressBar
        showProgressBar()

    }

    override fun addFavoriteList(list: List<FavoriteModel>) {
        (bind.rvFavoriteProduct.adapter as FavoriteAdapter).addList(list)
        bind.rvFavoriteProduct.visibility = View.VISIBLE
        bind.lnNotFound.visibility = View.GONE
    }
    override fun setError(error: String) {
        bind.lnNotFound.visibility = View.VISIBLE
        bind.rvFavoriteProduct.visibility = View.GONE
    }

    override fun setService() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = this.let { ApiClient.getApiClient(it).create(FavoriteApiService::class.java) }
        presenter = FavoritePresenter(coroutineScope, service)
    }

    override fun getSavedCostumerID() {
        costumerID = intent.getIntExtra("cs_id", 0)
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

    override fun setFavoriteList() {
        presenter?.getFavorite(2)
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