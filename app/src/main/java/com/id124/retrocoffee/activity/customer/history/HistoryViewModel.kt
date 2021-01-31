package com.id124.retrocoffee.activity.customer.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.account.LoginResponse
import com.id124.retrocoffee.model.history.HistoryModel
import com.id124.retrocoffee.model.history.HistoryResponse
import com.id124.retrocoffee.model.order.OrderModel
import com.id124.retrocoffee.model.order.OrderResponse
import com.id124.retrocoffee.service.HistoryApiService
import com.id124.retrocoffee.service.OrderApiService
import com.id124.retrocoffee.util.SharedPreference
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class HistoryViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: OrderApiService

    val onSuccessLiveData = MutableLiveData<List<OrderModel>>()
    val onFailLiveData = MutableLiveData<String>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: OrderApiService) {
        this@HistoryViewModel.service = service
    }

    fun serviceApi(id: Int) {
        launch {
            isLoadingLiveData.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.getAllOrder(
                    id
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoadingLiveData.value = false

                        when {
                            e.code() == 404 -> {
                                onFailLiveData.value = "History is empty!"
                            }
                            else -> {
                                onFailLiveData.value = "Server is maintenance!"
                            }
                        }
                    }
                }
            }

            if (response is OrderResponse) {
                isLoadingLiveData.value = false

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
                            it.orderDate )
                    }

                    onSuccessLiveData.value = list
                } else {
                    onFailLiveData.value = response.message
                }
            }
        }
    }
}