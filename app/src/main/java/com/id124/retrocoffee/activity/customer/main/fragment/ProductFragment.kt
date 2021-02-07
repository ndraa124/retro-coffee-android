package com.id124.retrocoffee.activity.customer.main.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.main.fragment.adapter.ProductAdapter
import com.id124.retrocoffee.activity.customer.product_detail.ProductDetailActivity
import com.id124.retrocoffee.base.BaseFragment
import com.id124.retrocoffee.databinding.FragmentProductBinding
import com.id124.retrocoffee.model.product.ProductModel

class ProductFragment(private val ctId: Int) : BaseFragment<FragmentProductBinding>() {
    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: ProductAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.fragment_product
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProductRecyclerView()
        setViewModel()
        subscribeLiveData()
    }

    override fun onStart() {
        super.onStart()
        viewModel.serviceGetApi(ctId)
    }

    override fun onResume() {
        super.onResume()
        viewModel.serviceGetApi(ctId)
    }

    private fun setProductRecyclerView() {
        bind.rvProduct.isNestedScrollingEnabled = true

        layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        bind.rvProduct.layoutManager = layoutManager

        adapter = ProductAdapter()
        bind.rvProduct.adapter = adapter

        adapter.setOnItemClickCallback(object : ProductAdapter.OnItemClickCallback {
            override fun onItemClick(data: ProductModel) {
                val intent = Intent(activity, ProductDetailActivity::class.java)
                intent.putExtra("ct_id", data.ctId)
                intent.putExtra("ct_name", data.ctName)
                intent.putExtra("pr_id", data.prId)
                intent.putExtra("pr_name", data.prName)
                intent.putExtra("pr_price", data.prPrice)
                intent.putExtra("pr_desc", data.prDesc)
                intent.putExtra("pr_discount", data.prDiscount)
                intent.putExtra("pr_discount_price", data.prDiscountPrice)
                intent.putExtra("pr_is_discount", data.prIsDiscount)
                intent.putExtra("pr_status", data.prStatus)
                intent.putExtra("pr_pic_image", data.prPicImage)
                startActivity(intent)
            }
        })
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@ProductFragment).get(ProductViewModel::class.java)
        viewModel.setService(createApi(activity))
        viewModel.serviceGetApi(ctId)
    }

    private fun subscribeLiveData() {
        activity?.let {
            viewModel.isLoadingLiveData.observe(it, { isLoading ->
                if (isLoading) {
                    bind.progressBar.visibility = View.VISIBLE
                } else {
                    bind.progressBar.visibility = View.GONE
                }
            })
        }

        activity?.let {
            viewModel.onSuccessLiveData.observe(it, { list ->
                bind.tvDataNotFound.visibility = View.GONE
                adapter.addList(list)
            })
        }

        activity?.let {
            viewModel.onFailLiveData.observe(it, { message ->
                bind.dataNotFound = message
                bind.tvDataNotFound.visibility = View.VISIBLE
            })
        }
    }
}