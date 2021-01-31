package com.id124.retrocoffee.activity.customer.forgot_password.new_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.account.EmailModel
import com.id124.retrocoffee.model.account.PasswordResponse
import com.id124.retrocoffee.model.account.RegisterResponse
import com.id124.retrocoffee.service.AccountApiService
import com.id124.retrocoffee.service.CustomerApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class PasswordViewModel: ViewModel(), CoroutineScope {
    private lateinit var accountService: AccountApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private val listacc = MutableLiveData<List<PasswordResponse.itemData>>()
    private val onSuccessReset = MutableLiveData<Boolean>()
    private val onSuccessUpdate = MutableLiveData<Boolean>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun setServiceAccount(service: AccountApiService) {
        this.accountService = service
    }
    fun ResetPassword(acId: String, password: String) {
        launch {
            isLoading.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    accountService.resetPassword(acId, password)
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
            if (response is PasswordResponse) {
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