package com.id124.retrocoffee.activity.customer.favorite

import com.id124.retrocoffee.model.favorite.FavoriteModel

interface FavoriteContract {
    interface View {
        fun addFavoriteList(list: List<FavoriteModel>)
        fun setError(error: String)
        fun setService()
        fun setBackButton()
        fun getSavedCostumerID()
        fun setRecyclerView()
        fun dataRefreshManagement()
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun getFavorite(costumerID: Int)
    }
}