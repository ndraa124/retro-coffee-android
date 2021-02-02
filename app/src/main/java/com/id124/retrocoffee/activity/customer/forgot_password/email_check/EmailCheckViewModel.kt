package com.id124.retrocoffee.activity.customer.forgot_password.email_check

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.account.VerifyResponse
import com.id124.retrocoffee.service.AccountApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class EmailCheckViewModel: ViewModel(),CoroutineScope {
    private lateinit var service: AccountApiService

    val onSuccess = MutableLiveData<Int>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: AccountApiService) {
        this@EmailCheckViewModel.service = service
    }

    fun serviceApi(email: String) {
        launch {
            isLoading.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.cekEmail(
                        email = email
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoading.value = false

                        when {
                            e.code() == 404 -> {
                                onFail.value = "Account not registered"
                            }
                            else -> {
                                onFail.value = "Email is fail! Please try again later!"
                            }
                        }
                    }
                }
            }

            if (response is VerifyResponse) {
                isLoading.value = false

                if (response.success) {
                    val data = response.data
                    onSuccess.value = data.acid
                } else {
                    onFail.value = response.message
                }
            }
        }
    }
}