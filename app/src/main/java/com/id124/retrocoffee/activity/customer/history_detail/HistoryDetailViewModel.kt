package com.id124.retrocoffee.activity.customer.history_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.history.HistoryModel
import com.id124.retrocoffee.model.history.HistoryResponse
import com.id124.retrocoffee.model.order.OrderModel
import com.id124.retrocoffee.model.order.OrderResponse
import com.id124.retrocoffee.service.HistoryApiService
import com.id124.retrocoffee.service.OrderApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class HistoryDetailViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: HistoryApiService

    val onSuccessLiveData = MutableLiveData<Boolean>()
    val onFailLiveData = MutableLiveData<String>()
    val isLoadingLiveData = MutableLiveData<Boolean>()
    var listHistory= MutableLiveData<List<HistoryModel>>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: HistoryApiService) {
        this@HistoryDetailViewModel.service = service
    }

    fun getAllHistory(): LiveData<List<HistoryModel>> {
        return listHistory
    }

    fun serviceApi(id: Int) {
        launch {
            isLoadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getAllHistoryOrder(
                        id
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        onSuccessLiveData.value = false

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

            if (response is HistoryResponse) {
                isLoadingLiveData.value = false
                if (response.success) {
                    Log.d("a" , response.toString())
                    onSuccessLiveData.value = true
                    val list = response.data?.map {
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
                    listHistory.value = list
                } else {
                    onFailLiveData.value = response.message
                }
            }
        }
    }
}