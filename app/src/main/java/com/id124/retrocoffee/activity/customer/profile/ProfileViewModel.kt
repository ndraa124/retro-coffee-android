package com.id124.retrocoffee.activity.customer.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.history.HistoryModel
import com.id124.retrocoffee.model.history.HistoryResponse
import com.id124.retrocoffee.service.HistoryApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class ProfileViewModel : ViewModel(), CoroutineScope {
    private lateinit var serviceHistory: HistoryApiService

    val onSuccessHistory = MutableLiveData<List<HistoryModel>>()
    val onFailHistory = MutableLiveData<String>()
    val isLoadingHistory = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setServiceHistory(service: HistoryApiService) {
        this.serviceHistory = service
    }

    fun serviceGetHistory(csId: Int) {
        launch {
            isLoadingHistory.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    serviceHistory.getAllHistory(csId)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoadingHistory.value = false

                        when {
                            e.code() == 404 -> {
                                onFailHistory.value = "History is empty!"
                            }
                            else -> {
                                onFailHistory.value = "Server is maintenance"
                            }
                        }
                    }
                }
            }

            if (response is HistoryResponse) {
                isLoadingHistory.value = false

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

                    onSuccessHistory.value = list
                } else {
                    onFailHistory.value = response.message
                }
            }
        }
    }
}