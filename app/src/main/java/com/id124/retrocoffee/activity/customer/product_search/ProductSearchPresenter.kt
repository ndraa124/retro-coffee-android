package com.id124.retrocoffee.activity.customer.product_search

import com.id124.retrocoffee.model.product.ProductModel
import com.id124.retrocoffee.model.product.ProductResponse
import com.id124.retrocoffee.service.ProductApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ProductSearchPresenter (private val coroutineScope: CoroutineScope,
                              private val service: ProductApiService?) : ProductSearchContract.Presenter {

    private var view: ProductSearchContract.View? = null

    override fun bindToView(view: ProductSearchContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun getAllProductList() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAllProduct()
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 404 -> {
                                view?.setError("Product Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ProductResponse) {
                if (result.success){
                    val list = result.data?.map{
                        ProductModel(it.prId, it.ctId, it.prName, it.prPrice, it.prDesc, it.prDiscount, it.prDiscountPrice, it.prIsDiscount, it.prStatus, it.prPicImage, it.ctName)
                    }
                    view?.addProductList(list)
                    view?.hideProgressBar()
                }
                else {
                    view?.setError(result.message)
                    view?.hideProgressBar()
                }
            }
        }
    }

    override fun getProductByName(product: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.searchProductByName(product)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 404 -> {
                                view?.setError("Product Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ProductResponse) {
                if (result.success){
                    val list = result.data?.map{
                        ProductModel(it.prId, it.ctId, it.prName, it.prPrice, it.prDesc, it.prDiscount, it.prDiscountPrice, it.prIsDiscount, it.prStatus, it.prPicImage, it.ctName)
                    }
                    view?.addProductList(list)
                    view?.hideProgressBar()
                }
                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun getProductByCategory(product: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.searchProductByCategory(product)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 404 -> {
                                view?.setError("Product Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ProductResponse) {
                if (result.success){
                    val list = result.data?.map{
                        ProductModel(it.prId, it.ctId, it.prName, it.prPrice, it.prDesc, it.prDiscount, it.prDiscountPrice, it.prIsDiscount, it.prStatus, it.prPicImage, it.ctName)
                    }
                    view?.addProductList(list)
                    view?.hideProgressBar()
                }
                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun getByHigherPrice() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getProductByHigher()
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 404 -> {
                                view?.setError("Product Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ProductResponse) {
                if (result.success){
                    val list = result.data?.map{
                        ProductModel(it.prId, it.ctId, it.prName, it.prPrice, it.prDesc, it.prDiscount, it.prDiscountPrice, it.prIsDiscount, it.prStatus, it.prPicImage, it.ctName)
                    }
                    view?.addProductList(list)
                    view?.hideProgressBar()
                }
                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun getByLowerPrice() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getProductByLower()
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 404 -> {
                                view?.setError("Product Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ProductResponse) {
                if (result.success){
                    val list = result.data?.map{
                        ProductModel(it.prId, it.ctId, it.prName, it.prPrice, it.prDesc, it.prDiscount, it.prDiscountPrice, it.prIsDiscount, it.prStatus, it.prPicImage, it.ctName)
                    }
                    view?.addProductList(list)
                    view?.hideProgressBar()
                }
                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun searchByHigherPrice(product: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.searchProductByHigher(product)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 404 -> {
                                view?.setError("Product Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ProductResponse) {
                if (result.success){
                    val list = result.data?.map{
                        ProductModel(it.prId, it.ctId, it.prName, it.prPrice, it.prDesc, it.prDiscount, it.prDiscountPrice, it.prIsDiscount, it.prStatus, it.prPicImage, it.ctName)
                    }
                    view?.addProductList(list)
                    view?.hideProgressBar()
                }
                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun searchByLowerPrice(product: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.searchProductByLower(product)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 404 -> {
                                view?.setError("Product Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ProductResponse) {
                if (result.success){
                    val list = result.data?.map{
                        ProductModel(it.prId, it.ctId, it.prName, it.prPrice, it.prDesc, it.prDiscount, it.prDiscountPrice, it.prIsDiscount, it.prStatus, it.prPicImage, it.ctName)
                    }
                    view?.addProductList(list)
                    view?.hideProgressBar()
                }
                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }


}