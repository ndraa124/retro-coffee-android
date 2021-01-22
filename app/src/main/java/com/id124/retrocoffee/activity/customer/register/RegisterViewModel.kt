package com.id124.retrocoffee.activity.customer.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.account.RegisterResponse
import com.id124.retrocoffee.service.AccountApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class RegisterViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: AccountApiService

    val onSuccessLiveData = MutableLiveData<Boolean>()
    val onMessageLiveData = MutableLiveData<String>()
    val onFailLiveData = MutableLiveData<String>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: AccountApiService) {
        this@RegisterViewModel.service = service
    }

    fun serviceEngineerApi(acName: String, acEmail: String, acPhone: String, acPassword: String) {
        launch {
            isLoadingLiveData.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.signUpEngineerAccount(
                        acName = acName,
                        acEmail = acEmail,
                        acPhone = acPhone,
                        acPassword = acPassword,
                        acLevel = 1,
                        acStatus = 1
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        onSuccessLiveData.value = false

                        when {
                            e.code() == 400 -> {
                                onFailLiveData.value = "Email has registered!"
                            }
                            else -> {
                                onFailLiveData.value = "Fail to registration! Please try again later!"
                            }
                        }
                    }
                }
            }

            if (response is RegisterResponse) {
                isLoadingLiveData.value = false

                if (response.success) {
                    onSuccessLiveData.value = true
                    onMessageLiveData.value = response.message
                } else {
                    onFailLiveData.value = response.message
                }
            }
        }
    }
}