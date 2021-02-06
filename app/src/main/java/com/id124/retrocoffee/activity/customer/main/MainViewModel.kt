package com.id124.retrocoffee.activity.customer.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.id124.retrocoffee.model.cart.CartModel
import com.id124.retrocoffee.model.cart.CartResponse
import com.id124.retrocoffee.model.category.CategoryModel
import com.id124.retrocoffee.model.category.CategoryResponse
import com.id124.retrocoffee.model.product.ProductModel
import com.id124.retrocoffee.model.product.ProductResponse
import com.id124.retrocoffee.service.CartApiService
import com.id124.retrocoffee.service.CategoryApiService
import com.id124.retrocoffee.service.ProductApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class MainViewModel : ViewModel(), CoroutineScope {
    private lateinit var serviceCategory: CategoryApiService
    private lateinit var serviceProduct: ProductApiService
    private lateinit var serviceCart: CartApiService

    val onSuccessCategory = MutableLiveData<List<CategoryModel>>()
    val onFailCategory = MutableLiveData<String>()
    val isLoadingCategory = MutableLiveData<Boolean>()

    val onSuccessProduct = MutableLiveData<List<ProductModel>>()
    val onFailProduct = MutableLiveData<String>()
    val isLoadingProduct = MutableLiveData<Boolean>()

    val onSuccessCart = MutableLiveData<List<CartModel>>()
    val onFailCart = MutableLiveData<String>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setServiceCategory(service: CategoryApiService) {
        this@MainViewModel.serviceCategory = service
    }

    fun setServiceProduct(service: ProductApiService) {
        this@MainViewModel.serviceProduct = service
    }

    fun setServiceCart(service: CartApiService) {
        this@MainViewModel.serviceCart = service
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

    fun serviceGetPromoApi() {
        launch {
            isLoadingProduct.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    serviceProduct.getAllProductByPromo(
                        limit = 5
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoadingProduct.value = false

                        when {
                            e.code() == 404 -> {
                                onFailProduct.value = "Product is empty!"
                            }
                            else -> {
                                onFailProduct.value = "Server is maintenance!"
                            }
                        }
                    }
                }
            }

            if (response is ProductResponse) {
                isLoadingProduct.value = false

                if (response.success) {
                    onSuccessProduct.value = response.data
                } else {
                    onFailProduct.value = response.message
                }
            }
        }
    }

    fun serviceGetCartApi(csId: Int) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceCart.getAllCart(
                        csId = csId
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        when {
                            e.code() == 404 -> {
                                onFailCart.value = "Cart is empty!"
                            }
                            else -> {
                                onFailCart.value = "Server is maintenance!"
                            }
                        }
                    }
                }
            }

            if (response is CartResponse) {
                if (response.success) {
                    onSuccessCart.value = response.data
                } else {
                    onFailCart.value = response.message
                }
            }
        }
    }
}