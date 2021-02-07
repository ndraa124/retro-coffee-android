package com.id124.retrocoffee.activity.admin.product.form_product

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE
import com.id124.retrocoffee.util.FileHelper
import com.id124.retrocoffee.util.FileHelper.Companion.createPartFromFile
import com.id124.retrocoffee.util.FileHelper.Companion.createPartFromString
import com.id124.retrocoffee.util.form_validate.ValidateProduct.Companion.valDescription
import com.id124.retrocoffee.util.form_validate.ValidateProduct.Companion.valName
import com.id124.retrocoffee.util.form_validate.ValidateProduct.Companion.valPercent
import com.id124.retrocoffee.util.form_validate.ValidateProduct.Companion.valPrice

class FormProductActivity : BaseActivity<ActivityFormProductBinding>(), View.OnClickListener {
    private lateinit var viewModel: FormProductViewModel

    private var ctId: Int? = 0
    private var prId: Int? = 0
    private var isPromote: Int? = 0
    private var pathImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_form_product
        super.onCreate(savedInstanceState)
        Log.e("PR ID", "${intent.getIntExtra("pr_id", 0)}")
        prId = intent.getIntExtra("pr_id", 0)

        setToolbarActionBar()
        setDataFromIntent()
        initTextWatcher()
        setCategoryAdapter()
        setPromoteProduct()
        setViewModel()
        subscribeCategoryLiveData()
        subscribeProductLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_edit_image -> {
                pickImageFromGallery()
            }
            R.id.btn_save -> {
                if (prId != 0) {
                    val totalDiscount: Double
                    val priceDiscount: Long

                    if (isPromote == 0) {
                        priceDiscount = 0
                    } else {
                        totalDiscount = (bind.etPrice.text.toString().toLong() / 100) * bind.etPercent.text.toString().toDouble()
                        priceDiscount = bind.etPrice.text.toString().toLong() - totalDiscount.toLong()
                    }

                    if (pathImage == null) {
                        when {
                            ctId == 0 -> {
                                noticeToast("Please choose category first!")
                            }
                            !valName(bind.inputLayoutProductName, bind.etName) -> {}
                            !valPrice(bind.inputLayoutProductPrice, bind.etPrice) -> {}
                            !valDescription(bind.inputLayoutProductDesc, bind.etDesc) -> {}
                            else -> {
                                viewModel.updateProduct(
                                    prId = prId!!,
                                    ctId = createPartFromString(ctId!!.toString()),
                                    prName = createPartFromString(bind.etName.text.toString()),
                                    prPrice = createPartFromString(bind.etPrice.text.toString()),
                                    prDesc = createPartFromString(bind.etDesc.text.toString()),
                                    prDiscount = createPartFromString(bind.etPercent.text.toString()),
                                    prDiscountPrice = createPartFromString(priceDiscount.toString()),
                                    prIsDiscount = createPartFromString(isPromote.toString())
                                )
                            }
                        }
                    } else {
                        when {
                            ctId == 0 -> {
                                noticeToast("Please choose category first!")
                            }
                            !valName(bind.inputLayoutProductName, bind.etName) -> {}
                            !valPrice(bind.inputLayoutProductPrice, bind.etPrice) -> {}
                            !valDescription(bind.inputLayoutProductDesc, bind.etDesc) -> {}
                            else -> {
                                viewModel.updateProduct(
                                    prId = prId!!,
                                    ctId = createPartFromString(ctId!!.toString()),
                                    prName = createPartFromString(bind.etName.text.toString()),
                                    prPrice = createPartFromString(bind.etPrice.text.toString()),
                                    prDesc = createPartFromString(bind.etDesc.text.toString()),
                                    prDiscount = createPartFromString(bind.etPercent.text.toString()),
                                    prDiscountPrice = createPartFromString(priceDiscount.toString()),
                                    prIsDiscount = createPartFromString(isPromote.toString()),
                                    image = createPartFromFile(pathImage!!)
                                )
                            }
                        }
                    }
                } else {
                    val totalDiscount: Double
                    val priceDiscount: Long

                    if (isPromote == 0) {
                        priceDiscount = "0".toLong()
                    } else {
                        totalDiscount = (bind.etPrice.text.toString().toLong() / 100) * bind.etPercent.text.toString().toDouble()
                        priceDiscount = bind.etPrice.text.toString().toLong() - totalDiscount.toLong()
                    }

                    if (pathImage == null) {
                        noticeToast("Please choose image first!")
                    } else {
                        when {
                            ctId == 0 -> {
                                noticeToast("Please choose category first!")
                            }
                            !valName(bind.inputLayoutProductName, bind.etName) -> {
                            }
                            !valPrice(bind.inputLayoutProductPrice, bind.etPrice) -> {
                            }
                            !valDescription(bind.inputLayoutProductDesc, bind.etDesc) -> {
                            }
                            else -> {
                                if (isPromote == 0) {
                                    viewModel.addProduct(
                                        ctId = createPartFromString(ctId!!.toString()),
                                        prName = createPartFromString(bind.etName.text.toString()),
                                        prPrice = createPartFromString(bind.etPrice.text.toString()),
                                        prDesc = createPartFromString(bind.etDesc.text.toString()),
                                        prDiscount = createPartFromString("0"),
                                        prDiscountPrice = createPartFromString(priceDiscount.toString()),
                                        prIsDiscount = createPartFromString(isPromote.toString()),
                                        image = createPartFromFile(pathImage!!)
                                    )
                                } else {
                                    viewModel.addProduct(
                                        ctId = createPartFromString(ctId!!.toString()),
                                        prName = createPartFromString(bind.etName.text.toString()),
                                        prPrice = createPartFromString(bind.etPrice.text.toString()),
                                        prDesc = createPartFromString(bind.etDesc.text.toString()),
                                        prDiscount = createPartFromString(bind.etPercent.text.toString()),
                                        prDiscountPrice = createPartFromString(priceDiscount.toString()),
                                        prIsDiscount = createPartFromString(isPromote.toString()),
                                        image = createPartFromFile(pathImage!!)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            R.id.cb_discount -> {
                if (bind.cbDiscount.isChecked) {
                    isPromote = 1

                    val animation: Animation = AnimationUtils.loadAnimation(
                        this@FormProductActivity,
                        R.anim.fade_in
                    )
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    noticeToast("Permission denied...!")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            bind.ivImageProduct.setImageURI(data?.data)
            pathImage = FileHelper.getPathFromURI(this@FormProductActivity, data?.data!!)
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

    private fun setDataFromIntent() {
        if (prId != 0) {
            bind.spCategory

            bind.etName.setText(intent.getStringExtra("pr_name"))
            bind.etPrice.setText(intent.getLongExtra("pr_price", 0).toString())
            bind.etDesc.setText(intent.getStringExtra("pr_desc"))

            if (intent.getIntExtra("pr_is_discount", 0) == 1) {
                isPromote = 1

                bind.cbDiscount.isChecked = true
                bind.clDiscount.visibility = View.VISIBLE
                bind.etPercent.setText(intent.getIntExtra("pr_discount", 0).toString())
            } else {
                isPromote = 0

                bind.cbDiscount.isChecked = false
                bind.clDiscount.visibility = View.GONE
                bind.etPercent.setText(intent.getIntExtra("pr_discount", 0).toString())
            }

            if (intent.getStringExtra("pr_pic_image") != null) {
                bind.imageUrl = BASE_URL_IMAGE + intent.getStringExtra("pr_pic_image")
            }
        }
    }

    private fun initTextWatcher() {
        bind.etName.addTextChangedListener(MyTextWatcher(bind.etName))
        bind.etPrice.addTextChangedListener(MyTextWatcher(bind.etPrice))
        bind.etDesc.addTextChangedListener(MyTextWatcher(bind.etDesc))

        if (isPromote == 1) {
            bind.etPercent.addTextChangedListener(MyTextWatcher(bind.etPercent))
        }
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

            if (prId != 0) {
                when {
                    intent.getIntExtra("ct_id", 0) == 1 -> {
                        ctId = 0
                    }
                    intent.getIntExtra("ct_id", 0) == 2 -> {
                        ctId = 1
                    }
                    intent.getIntExtra("ct_id", 0) == 3 -> {
                        ctId = 2
                    }
                    intent.getIntExtra("ct_id", 0) == 4 -> {
                        ctId = 3
                    }
                }

                bind.spCategory.setSelection(ctId!!)
            }

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

    private fun subscribeProductLiveData() {
        viewModel.isLoadingProduct.observe(this@FormProductActivity, {
            if (it) {
                bind.btnSave.visibility = View.GONE
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
                bind.btnSave.visibility = View.VISIBLE
            }
        })

        viewModel.onSuccessProduct.observe(this@FormProductActivity, {
            if (it) {
                noticeToast("Success to add product")
                this@FormProductActivity.finish()
            }
        })

        viewModel.onFailProduct.observe(this@FormProductActivity, {
            noticeToast(it)
        })
    }

    inner class MyTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.et_name -> valName(bind.inputLayoutProductName, bind.etName)
                R.id.et_price -> valPrice(bind.inputLayoutProductPrice, bind.etPrice)
                R.id.et_desc -> valDescription(bind.inputLayoutProductDesc, bind.etDesc)
                R.id.et_percent -> valPercent(bind.inputLayoutProductPercent, bind.etPercent)
            }
        }
    }
}