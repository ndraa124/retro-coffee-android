package com.id124.retrocoffee.activity.admin.product.form_product

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.admin.product.form_product.adapter.CategoryAdapter
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityFormProductBinding

class FormProductActivity : BaseActivity<ActivityFormProductBinding>(), View.OnClickListener {
    private lateinit var viewModel: FormProductViewModel
    private var ctId: Int? = 0
    private var isPromote: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_form_product
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setCategoryAdapter()
        setPromoteProduct()
        setViewModel()
        subscribeCategoryLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cb_discount -> {
                if (bind.cbDiscount.isChecked) {
                    isPromote = 1

                    val animation: Animation = AnimationUtils.loadAnimation(this@FormProductActivity, R.anim.fade_in)
                    bind.clDiscount.startAnimation(animation)
                    bind.clDiscount.visibility = View.VISIBLE

                    bind.etPercent.requestFocus()
                } else {
                    isPromote = 0
                    bind.clDiscount.visibility = View.GONE
                }
            }
            R.id.btn_back -> {
                onBackPressed()
            }
        }
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

    private fun setCategoryAdapter() {
        val adapter = CategoryAdapter(this@FormProductActivity)
        bind.spCategory.adapter = adapter
    }

    private fun setPromoteProduct() {
        bind.cbDiscount.setOnClickListener(this@FormProductActivity)
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@FormProductActivity).get(FormProductViewModel::class.java)
        viewModel.setServiceCategory(createApi(this@FormProductActivity))
        viewModel.setServiceProduct(createApi(this@FormProductActivity))
        viewModel.serviceGetCategoryApi()
    }

    private fun subscribeCategoryLiveData() {
        viewModel.onSuccessCategory.observe(this@FormProductActivity, { list ->
            (bind.spCategory.adapter as CategoryAdapter).addList(list)

            bind.spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val ct = list[position]
                    ctId = ct.ctId
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    return
                }
            }
        })

        viewModel.onFailCategory.observe(this@FormProductActivity, {
            Log.d("msg", it)
        })
    }


}