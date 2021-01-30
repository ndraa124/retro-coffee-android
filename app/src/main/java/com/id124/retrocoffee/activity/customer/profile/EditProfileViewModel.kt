package com.id124.retrocoffee.activity.customer.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.account.RegisterResponse
import com.id124.retrocoffee.model.customer.CustomerModel
import com.id124.retrocoffee.model.customer.CustomerResponse
import com.id124.retrocoffee.service.AccountApiService
import com.id124.retrocoffee.service.CustomerApiService
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class EditProfileViewModel: ViewModel(), CoroutineScope {

    private lateinit var accountService: AccountApiService
    private lateinit var customerService: CustomerApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    val listProfile = MutableLiveData<List<CustomerModel>>()
    val onSuccessProfile = MutableLiveData<Boolean>()
    val onSuccessUpdate = MutableLiveData<Boolean>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun setServiceProfile(service: CustomerApiService){
        this.customerService = service
    }

    fun setServiceAccount(service: AccountApiService) {
        this.accountService = service
    }

    fun getCustomerById(csId: Int){
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

    fun updateAccount(acId: Int, acName: String, acEmail: String, acPhone: String) {
        launch {
            isLoading.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    accountService.updateAccount(acId, acName, acEmail, acPhone)
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
            if (response is RegisterResponse) {
                isLoading.value = false
                if (response.success) {
                    onSuccessUpdate.value = true
                } else {
                    onFail.value = response.message
                }
            }
        }
    }

    fun updateCustomer(csId: Int, csGender: String, csDob: String, csAddress: String) {
        val gender = csGender.toRequestBody("text/plain".toMediaTypeOrNull())
        val dob = csDob.toRequestBody("text/plain".toMediaTypeOrNull())
        val address = csAddress.toRequestBody("text/plain".toMediaTypeOrNull())

        launch {
            isLoading.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    customerService.updateCustomer(csId, gender, dob, address)
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
            if (response is RegisterResponse) {
                isLoading.value = false
                if (response.success) {
                    onSuccessUpdate.value = true
                } else {
                    onFail.value = response.message
                }
            }
        }
    }

    fun updateCustomerWithImage(csId: Int, csGender: String, csDob: String, csAddress: String, image: MultipartBody.Part) {
        val gender = csGender.toRequestBody("text/plain".toMediaTypeOrNull())
        val dob = csDob.toRequestBody("text/plain".toMediaTypeOrNull())
        val address = csAddress.toRequestBody("text/plain".toMediaTypeOrNull())

        launch {
            isLoading.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    customerService.updateCustomerWithImage(csId, gender, dob, address, image)
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
            if (response is RegisterResponse) {
                isLoading.value = false
                if (response.success) {
                    onSuccessUpdate.value = true
                } else {
                    onFail.value = response.message
                }
            }
        }
    }
}