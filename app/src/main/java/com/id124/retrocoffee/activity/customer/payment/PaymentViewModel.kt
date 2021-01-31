package com.id124.retrocoffee.activity.customer.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.cart.CartModel
import com.id124.retrocoffee.model.cart.CartResponse
import com.id124.retrocoffee.model.order.OrderResponse
import com.id124.retrocoffee.service.CartApiService
import com.id124.retrocoffee.service.OrderApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class PaymentViewModel : ViewModel(), CoroutineScope {
    private lateinit var serviceCart: CartApiService
    private lateinit var serviceOrder: OrderApiService

    val isLoading = MutableLiveData<Boolean>()
    val isLoadingOrder = MutableLiveData<Boolean>()
    val onSuccess = MutableLiveData<List<CartModel>>()
    val onSuccessOrder = MutableLiveData<Boolean>()
    val onFail = MutableLiveData<String>()
    val onFailOrder = MutableLiveData<String>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setServiceCart(service: CartApiService) {
        this@PaymentViewModel.serviceCart = service
    }

    fun setServiceOrder(service: OrderApiService) {
        this@PaymentViewModel.serviceOrder = service
    }

    fun serviceGetApi(csId: Int) {
        launch {
            isLoading.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    serviceCart.getAllCart(
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

    fun serviceAddApi(csId: Int, orPayTotal: Long, orAddress: String, orNoteApprove: String, orMethodPayment: String, orFee: Long, orDateOrder: String) {
        launch {
            isLoadingOrder.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    serviceOrder.addOrder(
                        csId = csId,
                        orPayTotal = orPayTotal,
                        orAddress = orAddress,
                        orNoteCancel = "",
                        orNoteApprove = orNoteApprove,
                        orMethodPayment = orMethodPayment,
                        orFee = orFee,
                        orDateOrder = orDateOrder
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoadingOrder.value = false

                        when {
                            e.code() == 400 -> {
                                onFailOrder.value = "Fail to order product!"
                            }
                            else -> {
                                onFailOrder.value = "Server is maintenance!"
                            }
                        }
                    }
                }
            }

            if (response is OrderResponse) {
                isLoadingOrder.value = false

                if (response.success) {
                    onSuccessOrder.value = true
                } else {
                    onFailOrder.value = response.message
                }
            }
        }
    }
}