package com.id124.retrocoffee.activity.customer.forgot_password.email_check

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.account.EmailModel
import com.id124.retrocoffee.service.AccountApiService
import com.id124.retrocoffee.util.SharedPreference
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class EmailCheckModel: ViewModel(),CoroutineScope {
    private lateinit var service: AccountApiService
    private lateinit var sharedPref: SharedPreference

    val onSuccessLiveData = MutableLiveData<Boolean>()
    val onFailLiveData = MutableLiveData<String>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: AccountApiService) {
        this@EmailCheckModel.service = service
    }
    fun setSharedPref(sharedPref: SharedPreference) {
        this@EmailCheckModel.sharedPref = sharedPref
    }
    fun serviceApi(email: String) {
        launch {
            isLoadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    service.cekEmail(
                        email = email
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        onSuccessLiveData.value = false
                        when {
                            e.code() == 404 -> {
                                onFailLiveData.value = "Account not registered"
                            }
                            e.code() == 400 -> {
                                onFailLiveData.value = "Password is invalid!"
                            }
                            else -> {
                                onFailLiveData.value = "Email is fail! Please try again later!"
                            }
                        }
                    }
                }
            }
            if (response is EmailModel) {
                isLoadingLiveData.value = false
                if (response.success) {
                    val data = response.data
                    sharedPref.createEmail(
                        acId = data.acid
                    )
                    onSuccessLiveData.value = true
                } else {
                    onFailLiveData.value = response.message

                }
            }
        }
    }
}