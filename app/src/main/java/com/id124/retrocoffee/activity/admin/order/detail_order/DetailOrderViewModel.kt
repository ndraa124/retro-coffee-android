package com.id124.retrocoffee.activity.admin.order.detail_order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.history.HistoryModel
import com.id124.retrocoffee.model.history.HistoryResponse
import com.id124.retrocoffee.model.order_admin.OrdersResponse
import com.id124.retrocoffee.service.HistoryApiService
import com.id124.retrocoffee.service.OrderApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class DetailOrderViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: HistoryApiService
    private lateinit var serviceOrder: OrderApiService

    val onSuccess = MutableLiveData<List<HistoryModel>>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    val onSuccessOrder = MutableLiveData<Boolean>()
    val onFailOrder = MutableLiveData<String>()
    val isLoadingOrder = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: HistoryApiService) {
        this@DetailOrderViewModel.service = service
    }

    fun setServiceOrder(service: OrderApiService) {
        this@DetailOrderViewModel.serviceOrder = service
    }

    fun serviceGetApi(orId: Int) {
        launch {
            isLoading.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.getAllHistoryOrder(
                        orId = orId
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

            if (response is HistoryResponse) {
                isLoading.value = false

                if (response.success) {
                    val list = response.data.map {
                        HistoryModel(
                            it.historyId,
                            it.customerId,
                            it.orderId,
                            it.historyProduct,
                            it.historyPrice,
                            it.historyQty,
                            it.historyTotal,
                            it.historyImage
                        )
                    }

                    onSuccess.value = list
                } else {
                    onFail.value = response.message
                }
            }
        }
    }

    fun serviceUpdateApi(csId: Int, orId: Int, orStatus: Int) {
        launch {
            isLoadingOrder.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    serviceOrder.updateStatus(
                        csId = csId,
                        orId = orId,
                        orStatus = orStatus
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoadingOrder.value = false

                        when {
                            e.code() == 404 -> {
                                onFailOrder.value = "Data not found!"
                            }
                            else -> {
                                onFailOrder.value = "Server is maintenance!"
                            }
                        }
                    }
                }
            }

            if (response is OrdersResponse) {
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