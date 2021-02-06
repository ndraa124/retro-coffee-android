package com.id124.retrocoffee.activity.admin.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.product.ProductModel
import com.id124.retrocoffee.model.product.ProductResponse
import com.id124.retrocoffee.service.ProductApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class ProductViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: ProductApiService

    val onSuccess = MutableLiveData<List<ProductModel>>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: ProductApiService) {
        this@ProductViewModel.service = service
    }

    fun serviceGetApi() {
        launch {
            isLoading.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.getAllProduct()
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoading.value = false

                        when {
                            e.code() == 404 -> {
                                onFail.value = "Product is empty!"
                            }
                            else -> {
                                onFail.value = "Server is maintenance!"
                            }
                        }
                    }
                }
            }

            if (response is ProductResponse) {
                isLoading.value = false

                if (response.success) {
                    val list = response.data.map {
                        ProductModel(
                            ctId = it.ctId,
                            ctName = it.ctName,
                            prId = it.prId,
                            prName = it.prName,
                            prPrice = it.prPrice,
                            prDesc = it.prDesc,
                            prDiscount = it.prDiscount,
                            prDiscountPrice = it.prDiscountPrice,
                            prIsDiscount = it.prIsDiscount,
                            prStatus = it.prStatus,
                            prPicImage = it.prPicImage
                        )
                    }

                    onSuccess.value = list
                } else {
                    onFail.value = response.message
                }
            }
        }
    }
}