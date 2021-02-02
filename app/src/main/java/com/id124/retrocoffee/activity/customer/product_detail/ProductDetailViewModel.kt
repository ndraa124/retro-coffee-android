package com.id124.retrocoffee.activity.customer.product_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.cart.CartModel
import com.id124.retrocoffee.model.cart.CartResponse
import com.id124.retrocoffee.model.favorite.FavoriteResponse
import com.id124.retrocoffee.service.CartApiService
import com.id124.retrocoffee.service.FavoriteApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class ProductDetailViewModel : ViewModel(), CoroutineScope {
    private lateinit var serviceCart: CartApiService
    private lateinit var serviceFavorite: FavoriteApiService

    val onSuccessCart = MutableLiveData<String>()
    val onSuccessFavorite = MutableLiveData<String>()
    val onSuccessCheckFavorite = MutableLiveData<Boolean>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    val onSuccessCarts = MutableLiveData<List<CartModel>>()
    val onFailCart = MutableLiveData<String>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setServiceCart(service: CartApiService) {
        this@ProductDetailViewModel.serviceCart = service
    }

    fun setServiceFavorite(service: FavoriteApiService) {
        this@ProductDetailViewModel.serviceFavorite = service
    }

    fun serviceGetCartApi(csId: Int) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceCart.getAllCart(
                        csId = csId
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        when {
                            e.code() == 404 -> {
                                onFailCart.value = "Cart is empty!"
                            }
                            else -> {
                                onFailCart.value = "Server is maintenance!"
                            }
                        }
                    }
                }
            }

            if (response is CartResponse) {
                if (response.success) {
                    onSuccessCarts.value = response.data
                } else {
                    onFailCart.value = response.message
                }
            }
        }
    }

    fun serviceAddApi(
        csId: Int,
        crProduct: String,
        crPrice: Long,
        crQty: Int,
        crTotal: Long,
        crExpired: Int,
        crPicImage: String,
    ) {
        launch {
            isLoading.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    serviceCart.addCart(
                        csId = csId,
                        crProduct = crProduct,
                        crPrice = crPrice,
                        crQty = crQty,
                        crTotal = crTotal,
                        crExpired = crExpired,
                        crPicImage = crPicImage,
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoading.value = false

                        when {
                            e.code() == 404 -> {
                                onFail.value = "Data not found"
                            }
                            else -> {
                                onFail.value = "Server is maintenance!"
                            }
                        }
                    }
                }
            }

            if (response is CartResponse) {
                isLoading.value = false

                if (response.success) {
                    onSuccessCart.value = response.message
                } else {
                    onFail.value = response.message
                }
            }
        }
    }

    fun serviceCheckApi(csId: Int, prId: Int) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceFavorite.checkIsFavorite(
                        csId = csId,
                        prId = prId
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        onSuccessCheckFavorite.value = false

                        when {
                            e.code() == 404 -> {
                                onFail.value = "Data not found"
                            }
                            else -> {
                                onFail.value = "Server is maintenance!"
                            }
                        }
                    }
                }
            }

            if (response is FavoriteResponse) {
                onSuccessCheckFavorite.value = response.success
            }
        }
    }

    fun serviceAddFavoriteApi(csId: Int, prId: Int) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceFavorite.addFavorite(
                        csId = csId,
                        prId = prId
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        when {
                            e.code() == 404 -> {
                                onSuccessFavorite.value = "Data not found"
                            }
                            else -> {
                                onSuccessFavorite.value = "Server is maintenance!"
                            }
                        }
                    }
                }
            }

            if (response is FavoriteResponse) {
                onSuccessFavorite.value = response.message
            }
        }
    }
}