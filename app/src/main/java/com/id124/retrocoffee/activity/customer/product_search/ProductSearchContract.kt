package com.id124.retrocoffee.activity.customer.product_search

import com.id124.retrocoffee.model.product.ProductModel

interface ProductSearchContract {
    interface View {
        fun addProductList(list: List<ProductModel>)
        fun setService()
        fun setDataRefresh()
        fun setRecyclerView()
        fun setSearchFeature()
        fun popUpManager()
        fun quickPopUpManager()
        fun quickFilterListener()
        fun searchFilterListener(Query: String)
        fun setError(error: String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun getAllProductList()
        fun getProductByName(product: String)
        fun getProductByCategory(product: String)
        fun getByHigherPrice()
        fun getByLowerPrice()
        fun searchByHigherPrice(product: String)
        fun searchByLowerPrice(product: String)
    }
}