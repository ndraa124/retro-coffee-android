package com.id124.retrocoffee.activity.customer.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.category.CategoryModel
import com.id124.retrocoffee.model.category.CategoryResponse
import com.id124.retrocoffee.service.CategoryApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class MainViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: CategoryApiService

    val onSuccessLiveData = MutableLiveData<List<CategoryModel>>()
    val onFailLiveData = MutableLiveData<String>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: CategoryApiService) {
        this@MainViewModel.service = service
    }

    fun serviceGetApi() {
        launch {
            isLoadingLiveData.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.getAllCategory()
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoadingLiveData.value = false

                        when {
                            e.code() == 404 -> {
                                onFailLiveData.value = "Category is empty!"
                            }
                            else -> {
                                onFailLiveData.value = "Server is maintenance!"
                            }
                        }
                    }
                }
            }

            if (response is CategoryResponse) {
                isLoadingLiveData.value = false

                if (response.success) {
                    val list = response.data.map {
                        CategoryModel(
                            ctId = it.ctId,
                            ctName = it.ctName,
                            ctStatus = it.ctStatus,
                            ctPicImage = it.ctPicImage
                        )
                    }

                    onSuccessLiveData.value = list
                } else {
                    onFailLiveData.value = response.message
                }
            }
        }
    }
}