package com.id124.retrocoffee.activity.customer.favorite

import com.id124.retrocoffee.model.favorite.FavoriteModel
import com.id124.retrocoffee.model.favorite.FavoriteResponse
import com.id124.retrocoffee.service.FavoriteApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class FavoritePresenter (private val coroutineScope: CoroutineScope,
                         private val service: FavoriteApiService?) : FavoriteContract.Presenter {

    private var view: FavoriteContract.View? = null

    override fun bindToView(view: FavoriteContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun getFavorite(costumerID: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getFavoriteProduct(costumerID)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 404 -> {
                                view?.setError("Favorite Product Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is FavoriteResponse) {
                if (result.success){
                    val list = result.data?.map{
                        FavoriteModel(it.faId, it.csId, it.prID, it.prName, it.prPrice, it.prDesc, it.prPic)
                    }
                    view?.addFavoriteList(list)
                    view?.hideProgressBar()
                }
                else {
                    view?.setError(result.message)
                    view?.hideProgressBar()
                }
            }
        }
    }

    override fun deleteFavorite(favoriteID: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.deleteFavorite(favoriteID)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 404 -> {
                                view?.setError("Favorite Product Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is FavoriteResponse) {
                if (result.success){
                    val list = result.data?.map{
                        FavoriteModel(it.faId, it.csId, it.prID, it.prName, it.prPrice, it.prDesc, it.prPic)
                    }
                    view?.addFavoriteList(list)
                    view?.hideProgressBar()
                }
                else {
                    view?.setError(result.message)
                    view?.hideProgressBar()
                }
            }
        }
    }

}