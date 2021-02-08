package com.id124.retrocoffee.activity.admin.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.order.OrderModel
import com.id124.retrocoffee.model.order.OrderResponse
import com.id124.retrocoffee.service.OrderApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class OrderViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: OrderApiService

    val onSuccess = MutableLiveData<List<OrderModel>>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: OrderApiService) {
        this@OrderViewModel.service = service
    }

    fun serviceGetApi() {
        launch {
            isLoading.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.getAllOrderCustomer()
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoading.value = false

                        when {
                            e.code() == 404 -> {
                                onFail.value = "History is empty!"
                            }
                            else -> {
                                onFail.value = "Server is maintenance!"
                            }
                        }
                    }
                }
            }

            if (response is OrderResponse) {
                isLoading.value = false

                if (response.success) {
                    val list = response.data?.map {
                        OrderModel(
                            it.orderId,
                            it.customerId,
                            it.orderPayTotal,
                            it.orderAddress,
                            it.orderLatitude,
                            it.orderLongitude,
                            it.orderStatus,
                            it.orderNoteCancel,
                            it.orderNoteApprove,
                            it.orderMethodPayment,
                            it.orderFee,
                            it.orderDate
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