package com.id124.retrocoffee.activity.customer.favorite.favorite_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.favorite.FavoriteResponse
import com.id124.retrocoffee.service.FavoriteApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class FavoriteDetailViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: FavoriteApiService

    val onSuccessLiveData = MutableLiveData<Boolean>()
    val onFailLiveData = MutableLiveData<String>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: FavoriteApiService) {
        this.service = service
    }

    fun deleteFavorite(favoriteID: Int) {
        launch {
            isLoadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    service.deleteFavorite(
                        favoriteID
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        onSuccessLiveData.value = false
                        when {
                            e.code() == 404 -> {
                                onFailLiveData.value = "Favorite Product Not Found!"
                            }
                            else -> {
                                onFailLiveData.value = "Unknown Error, Please Try Again Later!"
                            }
                        }
                    }
                }
            }

            if (response is FavoriteResponse) {
                isLoadingLiveData.value = false

                if (response.success) {
                    onSuccessLiveData.value = true
                } else {
                    onFailLiveData.value = response.message
                }
            }
        }
    }
}