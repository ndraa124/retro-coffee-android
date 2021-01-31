package com.id124.retrocoffee.activity.customer.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.customer.CustomerUpdateResponse
import com.id124.retrocoffee.service.AccountApiService
import com.id124.retrocoffee.service.CustomerApiService
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class EditProfileViewModel : ViewModel(), CoroutineScope {
    private lateinit var accountService: AccountApiService
    private lateinit var customerService: CustomerApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    val onSuccess = MutableLiveData<String>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun setServiceAccount(service: AccountApiService) {
        this.accountService = service
    }

    fun setServiceCustomer(service: CustomerApiService) {
        this.customerService = service
    }

    fun updateAccount(
        acId: Int,
        acName: String,
        acEmail: String,
        acPhone: String,
        csId: Int,
        csGender: RequestBody,
        csDob: RequestBody,
        csAddress: RequestBody,
        image: MultipartBody.Part? = null
    ) {
        launch {
            isLoading.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    accountService.updateAccount(
                        accountId = acId,
                        accountName = acName,
                        accountEmail = acEmail,
                        accountPhone = acPhone
                    )

                    customerService.updateCustomer(
                        csId = csId,
                        csGender = csGender,
                        csDob = csDob,
                        csAddress = csAddress,
                        image = image
                    )
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

            if (response is CustomerUpdateResponse) {
                isLoading.value = false

                if (response.success) {
                    onSuccess.value = response.image
                } else {
                    onFail.value = response.message
                }
            }
        }
    }

    /*fun updateCustomer(
        csId: Int,
        csGender: RequestBody,
        csDob: RequestBody,
        csAddress: RequestBody,
        image: MultipartBody.Part? = null
    ) {
        launch {
            isLoading.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    customerService.updateCustomer(
                        csId = csId,
                        csGender = csGender,
                        csDob = csDob,
                        csAddress = csAddress,
                        image = image
                    )
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

            if (response is CustomerUpdateResponse) {
                isLoading.value = false

                if (response.success) {
                    onSuccess.value = true
                    onSuccessCs.value = response.image
                } else {
                    onFail.value = response.message
                }
            }
        }
    }*/
}