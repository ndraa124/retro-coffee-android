package com.id124.retrocoffee.activity.admin.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.order_admin.OrdersResponse
import com.id124.retrocoffee.model.order_admin.OrderModel
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

            if (response is OrdersResponse) {
                isLoading.value = false

                if (response.success) {
                    val list = response.data.map {
                        OrderModel(
                            orId = it.orId,
                            csId = it.csId,
                            orPayTotal = it.orPayTotal,
                            orAddress = it.orAddress,
                            orLatitude = it.orLatitude,
                            orLongitude = it.orLongitude,
                            orStatus = it.orStatus,
                            orNoteCancel = it.orNoteCancel,
                            orNoteApprove = it.orNoteApprove,
                            orMethodPayment = it.orMethodPayment,
                            orFee = it.orFee,
                            orDateOrder = it.orDateOrder,
                            historyProduct = it.historyProduct
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