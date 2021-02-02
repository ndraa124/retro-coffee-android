package com.id124.retrocoffee.activity.customer.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.login.LoginViewModel
import com.id124.retrocoffee.activity.customer.main.MainActivity
import com.id124.retrocoffee.activity.customer.welcome.WelcomeActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityEditPasswordBinding
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valPassConf
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valPassword

class EditPasswordActivity : BaseActivity<ActivityEditPasswordBinding>(), View.OnClickListener {
    private lateinit var viewModel: EditPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_edit_password
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        initTextWatcher()
        setViewModel()
        subscribeLiveData()
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

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_change_password -> {
                when {
                    !valPassword(bind.inputLayoutOldPassword, bind.etOldPassword) -> {
                    }
                    !valPassword(bind.inputLayoutNewPassword, bind.etNewPassword) -> {
                    }
                    !valPassConf(bind.inputLayoutConfirmPassword, bind.etConfirmPassword, bind.etNewPassword) -> {
                    }
                    else -> {
                        viewModel.ResetPassword(acId = sharedPref.getAcId(), password = bind.etNewPassword.text.toString())
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        intents<MainActivity>(this)
        this.finish()
    }

    private fun initTextWatcher() {
        bind.etOldPassword.addTextChangedListener(MyTextWatcher(bind.etOldPassword))
        bind.etNewPassword.addTextChangedListener(MyTextWatcher(bind.etNewPassword))
        bind.etConfirmPassword.addTextChangedListener(MyTextWatcher(bind.etConfirmPassword))

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

    private fun setViewModel() {
        viewModel = ViewModelProvider(this).get(EditPasswordViewModel::class.java)
        viewModel.setService(createApi(this))
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this) {
            if (it) {
                bind.btnChangePassword.visibility = View.GONE
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
                bind.btnChangePassword.visibility = View.VISIBLE
            }
        }

        viewModel.onSuccessUpdate.observe(this) {
            if (it) {
                bind.progressBar.visibility = View.GONE
                bind.btnChangePassword.visibility = View.VISIBLE

                intents<MainActivity>(this)
                this.finish()
            } else {
                bind.progressBar.visibility = View.GONE
                bind.btnChangePassword.visibility = View.VISIBLE
            }
        }

        viewModel.onFail.observe(this) { message ->
            noticeToast(message)
        }

    }
}