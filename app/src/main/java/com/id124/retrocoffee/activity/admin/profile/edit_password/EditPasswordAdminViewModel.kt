package com.id124.retrocoffee.activity.admin.profile.edit_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.account.PasswordResponse
import com.id124.retrocoffee.service.AccountApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class EditPasswordAdminViewModel: ViewModel(), CoroutineScope {
    private lateinit var service : AccountApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    val onSuccessCheck = MutableLiveData<Boolean>()
    val onSuccessUpdate = MutableLiveData<Boolean>()
    val onFailCheck = MutableLiveData<String>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun setService(service: AccountApiService) {
        this.service = service
    }

    fun checkPassword(acId: Int, password: String) {
        launch {
            isLoading.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.checkPassword(
                        acId = acId,
                        password = password
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoading.value = false

                        when {
                            e.code() == 404 -> {
                                onFailCheck.value = "Data not found"
                            }
                            else -> {
                                onFailCheck.value = "Server is maintenance"
                            }
                        }
                    }
                }
            }

            if (response is PasswordResponse) {
                isLoading.value = false

                if (response.success) {
                    onSuccessCheck.value = true
                } else {
                    onFailCheck.value = response.message
                }
            }
        }
    }

    fun resetPassword(acId: Int, password: String) {
        launch {
            isLoading.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.resetPassword(acId, password)
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