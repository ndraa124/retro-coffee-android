package com.id124.retrocoffee.activity.customer.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.cart.CartModel
import com.id124.retrocoffee.model.cart.CartResponse
import com.id124.retrocoffee.service.CartApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class PaymentViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: CartApiService

    val onSuccess = MutableLiveData<List<CartModel>>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: CartApiService) {
        this@PaymentViewModel.service = service
    }

    fun serviceGetApi(csId: Int) {
        launch {
            isLoading.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.getAllCart(
                        csId = csId
                    )
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

            if (response is CartResponse) {
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