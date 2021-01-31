package com.id124.retrocoffee.activity.customer.forgot_password.new_password

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.account.LoginResponse
import com.id124.retrocoffee.model.account.ResetPasswordResponse
import com.id124.retrocoffee.service.AccountApiService
import com.id124.retrocoffee.util.SharedPreference
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class NewPasswordViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: AccountApiService
    private lateinit var sharedPref: SharedPreference

    val onSuccessLiveData = MutableLiveData<Boolean>()
    val onFailLiveData = MutableLiveData<String>()
    val isLoadingLiveData = MutableLiveData<Boolean>()
    val onMessageLiveData = MutableLiveData<String>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: AccountApiService) {
        this@NewPasswordViewModel.service = service
    }

    fun setSharedPref(sharedPref: SharedPreference) {
        this@NewPasswordViewModel.sharedPref = sharedPref
    }

    fun serviceApi(acId: Int, password: String) {
        launch {
            isLoadingLiveData.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.updatePassword(
                        accountId = acId,
                        acPassword = password
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        onSuccessLiveData.value = false

                        when {
                            e.code() == 404 -> {
                                onFailLiveData.value = "Failed Update Password"
                            }
                            e.code() == 400 -> {
                                onFailLiveData.value = "Password is invalid!"
                            }
                            else -> {
                                onFailLiveData.value = "Update is fail! Please try again later!"
                            }
                        }
                    }
                }
            }

            if (response is ResetPasswordResponse) {
                Log.d("Response Reset" , response.toString())
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