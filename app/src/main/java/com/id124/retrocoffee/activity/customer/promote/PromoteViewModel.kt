package com.id124.retrocoffee.activity.customer.promote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.product.ProductModel
import com.id124.retrocoffee.model.product.ProductResponse
import com.id124.retrocoffee.service.ProductApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class PromoteViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: ProductApiService

    val onSuccess = MutableLiveData<List<ProductModel>>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: ProductApiService) {
        this@PromoteViewModel.service = service
    }

    fun serviceGetApi() {
        launch {
            isLoading.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.getAllProductByPromo()
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
                    onSuccess.value = response.data
                } else {
                    onFail.value = response.message
                }
            }
        }
    }
}