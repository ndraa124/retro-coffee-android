package com.id124.retrocoffee.activity.admin.profile.edit_profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityEditProfileAdminBinding
import com.id124.retrocoffee.util.form_validate.ValidateAccount

class EditProfileAdminActivity : BaseActivity<ActivityEditProfileAdminBinding>(), View.OnClickListener {
    private lateinit var viewModel: EditProfileAdminViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_edit_profile_admin
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setDataSharedPref()
        initTextWatcher()
        setViewModel()
        subscribeLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_save -> {
                when {
                    !ValidateAccount.valName(bind.inputLayoutName, bind.etName) -> {}
                    !ValidateAccount.valEmail(bind.inputLayoutEmail, bind.etEmail) -> {}
                    !ValidateAccount.valPhoneNumber(bind.inputLayoutPhoneNumber, bind.etPhoneNumber) -> {}
                    else -> {
                        viewModel.updateAccount(
                            acId = sharedPref.getAcId(),
                            acName = bind.etName.text.toString(),
                            acEmail = bind.etEmail.text.toString(),
                            acPhone = bind.etPhoneNumber.text.toString()
                        )
                    }
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

    private fun setDataSharedPref() {
        bind.etName.setText(sharedPref.getAcName())
        bind.etEmail.setText(sharedPref.getAcEmail())
        bind.etPhoneNumber.setText(sharedPref.getAcPhone())
        bind.ivImageProfile.setImageResource(R.drawable.profile)
    }

    private fun initTextWatcher() {
        bind.etName.addTextChangedListener(MyTextWatcher(bind.etName))
        bind.etEmail.addTextChangedListener(MyTextWatcher(bind.etEmail))
        bind.etPhoneNumber.addTextChangedListener(MyTextWatcher(bind.etPhoneNumber))
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@EditProfileAdminActivity).get(EditProfileAdminViewModel::class.java)
        viewModel.setService(createApi(this@EditProfileAdminActivity))
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this@EditProfileAdminActivity) {
            if (it) {
                bind.btnSave.visibility = View.GONE
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
                bind.btnSave.visibility = View.VISIBLE
            }
        }

        viewModel.onSuccess.observe(this@EditProfileAdminActivity) {
            if (it) {
                sharedPref.createAcName(bind.etName.text.toString())
                sharedPref.createAcEmail(bind.etEmail.text.toString())
                sharedPref.createAcPhone(bind.etPhoneNumber.text.toString())

                noticeToast("Update profile success")
                this@EditProfileAdminActivity.finish()
            } else {
                noticeToast("Fail to update profile!")
                this@EditProfileAdminActivity.finish()
            }
        }

        viewModel.onFail.observe(this@EditProfileAdminActivity) { message ->
            noticeToast(message)
        }
    }

    inner class MyTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.et_name -> ValidateAccount.valName(bind.inputLayoutName, bind.etName)
                R.id.et_email -> ValidateAccount.valEmail(bind.inputLayoutEmail, bind.etEmail)
                R.id.et_phone_number -> ValidateAccount.valPhoneNumber(
                    bind.inputLayoutPhoneNumber,
                    bind.etPhoneNumber
                )
            }
        }
    }
}