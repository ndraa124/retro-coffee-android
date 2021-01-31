package com.id124.retrocoffee.activity.customer.forgot_password.email_check

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.account.CheckEmailResponse
import com.id124.retrocoffee.service.AccountApiService
import com.id124.retrocoffee.util.SharedPreference
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class EmailCheckViewModel: ViewModel(),CoroutineScope {
    private lateinit var service: AccountApiService
    private lateinit var sharedPref: SharedPreference

    val onSuccessLiveData = MutableLiveData<Boolean>()
    val onFailLiveData = MutableLiveData<String>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: AccountApiService) {
        this@EmailCheckViewModel.service = service
    }
    fun setSharedPref(sharedPref: SharedPreference) {
        this@EmailCheckViewModel.sharedPref = sharedPref
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
            if (response is CheckEmailResponse) {
                Log.d("a", response.toString())
                isLoadingLiveData.value = false
                if (response.success) {
                    onSuccessLiveData.value = true
                    sharedPref.createEmail(response.data.acId)
                } else {
                    onFailLiveData.value = response.message

                }
            }
        }
    }
}