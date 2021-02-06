package com.id124.retrocoffee.activity.admin.product

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.admin.product.adapter.ProductAdapter
import com.id124.retrocoffee.activity.admin.product.form_product.FormProductActivity
import com.id124.retrocoffee.activity.customer.product_detail.ProductDetailActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityProductBinding
import com.id124.retrocoffee.model.product.ProductModel
import com.id124.retrocoffee.util.Utils

class ProductActivity : BaseActivity<ActivityProductBinding>(), View.OnClickListener {
    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: ProductAdapter
    private lateinit var layoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_product
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setProductRecyclerView()
        setViewModel()
        subscribeLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_product -> {
                intents<FormProductActivity>(this@ProductActivity)
            }
            R.id.btn_back -> {
                onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.serviceGetApi()
    }

    private fun setToolbarActionBar() {
        setStatusBar()
        setSupportActionBar(bind.toolbar)
    }

    private fun setStatusBar() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = this.resources.getColor(R.color.background, theme)
    }

    private fun setProductRecyclerView() {
        val offsetPx = resources.getDimension(R.dimen.bottom_end_recyclerview)
        val bottomOffsetDecoration = Utils.Companion.BottomOffsetDecoration(offsetPx.toInt())

        bind.rvProduct.addItemDecoration(bottomOffsetDecoration)
        bind.rvProduct.setHasFixedSize(true)

        layoutManager = GridLayoutManager(
            this@ProductActivity,
            2,
            GridLayoutManager.VERTICAL,
            false
        )

        bind.rvProduct.layoutManager = layoutManager

        adapter = ProductAdapter()
        bind.rvProduct.adapter = adapter

        adapter.setOnItemClickCallback(object : ProductAdapter.OnItemClickCallback {
            override fun onItemClick(data: ProductModel) {
                val intent = Intent(this@ProductActivity, ProductDetailActivity::class.java)
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
        viewModel = ViewModelProvider(this@ProductActivity).get(ProductViewModel::class.java)
        viewModel.setService(createApi(this@ProductActivity))
        viewModel.serviceGetApi()
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this@ProductActivity) {
            if (it) {
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
            }
        }

        viewModel.onSuccess.observe(this@ProductActivity) { list ->
            adapter.addList(list)
            bind.tvDataNotFound.visibility = View.GONE
        }

        viewModel.onFail.observe(this@ProductActivity) { message ->
            bind.tvDataNotFound.text = message
            bind.tvDataNotFound.visibility = View.VISIBLE
        }
    }
}