package com.id124.retrocoffee.activity.admin.product.form_product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.category.CategoryModel
import com.id124.retrocoffee.model.category.CategoryResponse
import com.id124.retrocoffee.model.product.ProductModel
import com.id124.retrocoffee.service.CategoryApiService
import com.id124.retrocoffee.service.ProductApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class FormProductViewModel : ViewModel(), CoroutineScope {
    private lateinit var serviceCategory: CategoryApiService
    private lateinit var serviceProduct: ProductApiService

    val onSuccessCategory = MutableLiveData<List<CategoryModel>>()
    val onFailCategory = MutableLiveData<String>()
    val isLoadingCategory = MutableLiveData<Boolean>()

    val onSuccessProduct = MutableLiveData<List<ProductModel>>()
    val onFailProduct = MutableLiveData<String>()
    val isLoadingProduct = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setServiceCategory(service: CategoryApiService) {
        this@FormProductViewModel.serviceCategory = service
    }

    fun setServiceProduct(service: ProductApiService) {
        this@FormProductViewModel.serviceProduct = service
    }

    fun serviceGetCategoryApi() {
        launch {
            isLoadingCategory.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    serviceCategory.getAllCategory()
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoadingCategory.value = false

                        when {
                            e.code() == 404 -> {
                                onFailCategory.value = "Category is empty!"
                            }
                            else -> {
                                onFailCategory.value = "Server is maintenance!"
                            }
                        }
                    }
                }
            }

            if (response is CategoryResponse) {
                isLoadingCategory.value = false

                if (response.success) {
                    val list = response.data.map {
                        CategoryModel(
                            ctId = it.ctId,
                            ctName = it.ctName,
                            ctStatus = it.ctStatus,
                            ctPicImage = it.ctPicImage
                        )
                    }

                    onSuccessCategory.value = list
                } else {
                    onFailCategory.value = response.message
                }
            }
        }
    }
}