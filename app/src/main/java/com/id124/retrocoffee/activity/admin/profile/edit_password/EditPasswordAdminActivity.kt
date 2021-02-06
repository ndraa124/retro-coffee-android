package com.id124.retrocoffee.activity.admin.profile.edit_password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityEditPasswordAdminBinding
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valPassConf
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valPassword

class EditPasswordAdminActivity : BaseActivity<ActivityEditPasswordAdminBinding>(), View.OnClickListener {
    private lateinit var viewModel: EditPasswordAdminViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_edit_password_admin
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        initTextWatcher()
        setViewModel()
        subscribeLiveData()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_change_password -> {
                when {
                    !valPassword(bind.inputLayoutOldPassword, bind.etOldPassword) -> {}
                    !valPassword(bind.inputLayoutNewPassword, bind.etNewPassword) -> {}
                    !valPassConf(bind.inputLayoutConfirmPassword, bind.etConfirmPassword, bind.etNewPassword) -> {}
                    else -> {
                        viewModel.checkPassword(
                            acId = sharedPref.getAcId(),
                            password = bind.etOldPassword.text.toString()
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

    private fun initTextWatcher() {
        bind.etOldPassword.addTextChangedListener(MyTextWatcher(bind.etOldPassword))
        bind.etNewPassword.addTextChangedListener(MyTextWatcher(bind.etNewPassword))
        bind.etConfirmPassword.addTextChangedListener(MyTextWatcher(bind.etConfirmPassword))
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@EditPasswordAdminActivity).get(EditPasswordAdminViewModel::class.java)
        viewModel.setService(createApi(this@EditPasswordAdminActivity))
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this@EditPasswordAdminActivity) {
            if (it) {
                bind.btnChangePassword.visibility = View.GONE
                bind.progressBar.visibility = View.VISIBLE
            }
        }

        viewModel.onSuccessCheck.observe(this@EditPasswordAdminActivity) {
            if (it) {
                viewModel.resetPassword(
                    acId = sharedPref.getAcId(),
                    password = bind.etNewPassword.text.toString()
                )
            }
        }

        viewModel.onSuccessUpdate.observe(this@EditPasswordAdminActivity) {
            if (it) {
                bind.progressBar.visibility = View.GONE
                bind.btnChangePassword.visibility = View.VISIBLE

                noticeToast("Success to change password")
                this@EditPasswordAdminActivity.finish()
            }
        }

        viewModel.onFailCheck.observe(this@EditPasswordAdminActivity) {
            bind.progressBar.visibility = View.GONE
            bind.btnChangePassword.visibility = View.VISIBLE

            bind.inputLayoutOldPassword.isHelperTextEnabled = true
            bind.inputLayoutOldPassword.helperText = "Password is invalid!"
            bind.etOldPassword.requestFocus()
        }

        viewModel.onFail.observe(this@EditPasswordAdminActivity) { message ->
            bind.progressBar.visibility = View.GONE
            bind.btnChangePassword.visibility = View.VISIBLE

            noticeToast(message)
        }
    }

    inner class MyTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.et_old_password -> valPassword(bind.inputLayoutOldPassword, bind.etOldPassword)
                R.id.et_new_password -> valPassword(bind.inputLayoutNewPassword, bind.etNewPassword)
                R.id.et_confirm_password -> valPassConf(bind.inputLayoutConfirmPassword, bind.etConfirmPassword, bind.etNewPassword)
            }
        }
    }
}