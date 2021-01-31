package com.id124.retrocoffee.activity.customer.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.customer.CustomerModel
import com.id124.retrocoffee.model.customer.CustomerResponse
import com.id124.retrocoffee.model.history.HistoryModel
import com.id124.retrocoffee.model.history.HistoryResponse
import com.id124.retrocoffee.service.CustomerApiService
import com.id124.retrocoffee.service.HistoryApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class ProfileViewModel : ViewModel(), CoroutineScope {

    private lateinit var customerService: CustomerApiService
    private lateinit var historyService: HistoryApiService

    val listHistory = MutableLiveData<List<HistoryModel>>()
    val listProfile = MutableLiveData<List<CustomerModel>>()
    val onSuccessProfile = MutableLiveData<Boolean>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setServiceProfile(service: CustomerApiService) {
        this.customerService = service
    }

    fun getCustomerById(csId: Int) {
        launch {
            isLoading.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    customerService.getCustomerByCsId(csId)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoading.value = false
                        when {
                            e.code() == 404 -> {
                                onFail.value = "Data not found"
                            }

                            else -> {
                                onFail.value = "Server is maintenance"
                            }
                        }
                    }
                }
            }

            if (response is CustomerResponse) {
                isLoading.value = false
                if (response.success) {
                    listProfile.value = response.data
                    onSuccessProfile.value = true
                } else {
                    onSuccessProfile.value = false
                }
            }
        }
    }

    fun getHistoryById(csId: Int) {
        launch {
            isLoading.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    historyService.getAllHistoryOrder(csId)
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
                    listHistory.value = list
                } else {
                    onFail.value = response.message
                }
            }
        }
    }

}