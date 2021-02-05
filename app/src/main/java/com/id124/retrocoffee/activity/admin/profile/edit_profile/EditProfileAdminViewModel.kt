package com.id124.retrocoffee.activity.admin.profile.edit_profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.account.RegisterResponse
import com.id124.retrocoffee.service.AccountApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class EditProfileAdminViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: AccountApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    val onSuccess = MutableLiveData<Boolean>()
    val onFail = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun setService(service: AccountApiService) {
        this.service = service
    }

    fun updateAccount(
        acId: Int,
        acName: String,
        acEmail: String,
        acPhone: String
    ) {
        launch {
            isLoading.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.updateAccount(
                        accountId = acId,
                        accountName = acName,
                        accountEmail = acEmail,
                        accountPhone = acPhone
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

            if (response is RegisterResponse) {
                isLoading.value = false

                if (response.success) {
                    onSuccess.value = true
                } else {
                    onFail.value = response.message
                }
            }
        }
    }
}