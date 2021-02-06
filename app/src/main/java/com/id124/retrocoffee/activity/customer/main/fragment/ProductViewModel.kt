package com.id124.retrocoffee.activity.customer.main.fragment

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

    val onSuccessLiveData = MutableLiveData<List<ProductModel>>()
    val onFailLiveData = MutableLiveData<String>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: ProductApiService) {
        this@ProductViewModel.service = service
    }

    fun serviceGetApi(ctId: Int) {
        launch {
            isLoadingLiveData.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.getAllProductByCategory(
                        ctId = ctId,
                        limit = 5
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoadingLiveData.value = false

                        when {
                            e.code() == 404 -> {
                                onFailLiveData.value = "Product is empty!"
                            }
                            else -> {
                                onFailLiveData.value = "Server is maintenance!"
                            }
                        }
                    }
                }
            }

            if (response is ProductResponse) {
                isLoadingLiveData.value = false

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

                    onSuccessLiveData.value = list
                } else {
                    onFailLiveData.value = response.message
                }
            }
        }
    }
}