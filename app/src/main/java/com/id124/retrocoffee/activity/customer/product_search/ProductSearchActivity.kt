package com.id124.retrocoffee.activity.customer.product_search

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityProductSearchBinding
import com.id124.retrocoffee.model.product.ProductModel
import com.id124.retrocoffee.remote.ApiClient
import com.id124.retrocoffee.remote.ApiClient.Companion.getApiClient
import com.id124.retrocoffee.service.ProductApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class ProductSearchActivity : BaseActivity<ActivityProductSearchBinding>(), ProductSearchContract.View {

    private lateinit var coroutineScope: CoroutineScope
    private var presenter: ProductSearchPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_product_search

        presenter?.getAllProductList()

        super.onCreate(savedInstanceState)
    }

    override fun addProductList(list: List<ProductModel>) {
        (bind.rvProduct.adapter as ProductAdapter).addList(list)
    }

    override fun setService() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = this.let { getApiClient(it).create(ProductApiService::class.java) }
        presenter = ProductSearchPresenter(coroutineScope, service)
    }

    override fun setRecyclerView() {
        bind.rvProduct.adapter = ProductAdapter()
        bind.rvProduct.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

    }

    override fun setError(error: String) {
        TODO("Not yet implemented")
    }

    override fun showProgressBar() {
        TODO("Not yet implemented")
    }

    override fun hideProgressBar() {
        TODO("Not yet implemented")
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