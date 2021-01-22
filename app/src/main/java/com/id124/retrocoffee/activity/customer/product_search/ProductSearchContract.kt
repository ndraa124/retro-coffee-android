package com.id124.retrocoffee.activity.customer.product_search

import com.id124.retrocoffee.model.product.ProductModel

interface ProductSearchContract {
    interface View {
        fun addProductList(list: List<ProductModel>)
        fun setError(error: String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun getAllProductList()
        fun getProductByName(product: String)
        fun getByHigherPrice(product: String)
        fun getByLowerPrice(product: String)
    }
}