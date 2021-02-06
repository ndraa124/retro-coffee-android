package com.id124.retrocoffee.activity.customer.product_search

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityProductSearchBinding
import com.id124.retrocoffee.model.product.ProductModel
import com.id124.retrocoffee.remote.ApiClient.Companion.getApiClient
import com.id124.retrocoffee.service.ProductApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class ProductSearchActivity : BaseActivity<ActivityProductSearchBinding>(), ProductSearchContract.View {

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var handler: Handler
    private lateinit var popupMenu: PopupMenu

    private var keyword: String? = null
    private var presenter: ProductSearchPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_product_search
        super.onCreate(savedInstanceState)

        //Set Service
        setService()

        //Set Data Refresh
        setDataRefresh()

        //Set RecyclerView
        setRecyclerView()

        //Set Search
        setSearchFeature()

        //Set Pop Up
        quickPopUpManager()

        //Show Progressbar
        showProgressBar()

        //Set Without data filter
        bind.filter.setOnClickListener {
            handler.removeCallbacksAndMessages(null)
            quickFilterListener()
        }

        //Set Back Button
        setBackButton()

    }

    override fun addProductList(list: List<ProductModel>) {
        (bind.rvProduct.adapter as ProductSearchAdapter).addList(list)
        bind.rvProduct.visibility = View.VISIBLE
        bind.lnNotFound.visibility = View.GONE
    }

    override fun setService() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = this.let { getApiClient(it).create(ProductApiService::class.java) }
        presenter = ProductSearchPresenter(coroutineScope, service)
    }

    override fun setDataRefresh() {
        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                presenter?.getAllProductList()
                handler.postDelayed(this, 5000)
            }
        })
    }

    override fun setRecyclerView() {
        bind.rvProduct.adapter = ProductSearchAdapter()
        bind.rvProduct.layoutManager = GridLayoutManager(
            this,
            2,
            GridLayoutManager.VERTICAL,
            false
        )
    }

    override fun setSearchFeature() {
        bind.tbSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                bind.tbSearchBar.clearFocus()
                if (query != null) {
                    handler.removeCallbacksAndMessages(null)
                    bind.rvProduct.visibility = View.VISIBLE
                    keyword = query
                    presenter?.getProductByName(query)
                    popUpManager()
                    searchFilterListener(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    handler.removeCallbacksAndMessages(null)
                    if(newText.length >= 3){
                        keyword = newText
                        presenter?.getProductByName(newText)
                        popUpManager()
                        searchFilterListener(newText)
                    }
                }
                return false
            }
        })

        bind.tbSearchBar.setOnCloseListener {
            bind.tbSearchBar.clearFocus()
            presenter?.getAllProductList()
            false
        }
    }

    override fun setBackButton() {
        bind.btBackButton.setOnClickListener {
            finish()
        }
    }

    override fun popUpManager() {
        popupMenu = PopupMenu(this, bind.filter)
        popupMenu.menu.add(Menu.NONE, 0 ,0, "Category")
        popupMenu.menu.add(Menu.NONE, 1 ,1, "Higher Price")
        popupMenu.menu.add(Menu.NONE, 2 ,2, "Lower Price")
    }

    override fun quickPopUpManager() {
        popupMenu = PopupMenu(this, bind.filter)
        popupMenu.menu.add(Menu.NONE, 0 ,0, "Higher Price")
        popupMenu.menu.add(Menu.NONE, 1 ,1, "Lower Price")
    }

    override fun quickFilterListener() {
        bind.filter.setOnClickListener {
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    0 -> presenter?.getByHigherPrice()
                    1 -> presenter?.getByLowerPrice()
                }
                false
            }
            popupMenu.show()
        }
    }

    override fun searchFilterListener(Query: String) {
        bind.filter.setOnClickListener {
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    0 -> presenter?.getProductByCategory(Query)
                    1 -> presenter?.searchByHigherPrice(Query)
                    2 -> presenter?.searchByLowerPrice(Query)
                }
                false
            }
            popupMenu.show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setError(error: String) {
        bind.lnNotFound.visibility = View.VISIBLE
        bind.tvQueryNotfound.text = "Search result of $keyword is not found !"
        bind.rvProduct.visibility = View.GONE
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