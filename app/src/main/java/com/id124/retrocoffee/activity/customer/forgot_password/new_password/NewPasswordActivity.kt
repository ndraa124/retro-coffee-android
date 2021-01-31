package com.id124.retrocoffee.activity.customer.forgot_password.new_password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.login.LoginActivity
import com.id124.retrocoffee.activity.customer.register.RegisterViewModel
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityNewPasswordBinding
import com.id124.retrocoffee.util.form_validate.ValidateAccount
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valEmail
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valName
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valPassConf
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valPassword

class NewPasswordActivity : BaseActivity<ActivityNewPasswordBinding>(), View.OnClickListener {
    private lateinit var viewModel: NewPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_new_password
        super.onCreate(savedInstanceState)

        setViewModel()
        subscribeLiveData()
        initTextWatcher()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@NewPasswordActivity).get(NewPasswordViewModel::class.java)
        viewModel.setService(createApi(this@NewPasswordActivity))
    }

    private fun initTextWatcher() {
        bind.etPassword.addTextChangedListener(MyTextWatcher(bind.etPassword))
        bind.etPasswordConfirmation.addTextChangedListener(MyTextWatcher(bind.etPasswordConfirmation))
    }

    inner class MyTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.et_password -> valPassword(
                    bind.inputLayoutPassword,
                    bind.etPassword
                )
                R.id.et_password_confirmation -> valPassConf(
                    bind.inputLayoutPasswordConfirmation,
                    bind.etPasswordConfirmation,
                    bind.etPassword
                )
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_reset_password -> {
                when {
                    !valPassword(bind.inputLayoutPassword, bind.etPassword) -> {
                    }
                    !valPassConf(
                        bind.inputLayoutPasswordConfirmation,
                        bind.etPasswordConfirmation,
                        bind.etPassword
                    ) -> {
                    }
                    else -> {
                        viewModel.serviceApi(
                            acId = sharedPref.getAcId(),
                            password = bind.etPassword.text.toString()
                        )
                    }
                }
            }
        }
    }

    private fun subscribeLiveData() {
        viewModel.isLoadingLiveData.observe(this@NewPasswordActivity, {
            bind.btnResetPassword.visibility = View.GONE
            bind.progressBar.visibility = View.VISIBLE
        })

        viewModel.onSuccessLiveData.observe(this@NewPasswordActivity, {
            if (it) {
                bind.progressBar.visibility = View.GONE
                bind.btnResetPassword.visibility = View.VISIBLE
                intents<LoginActivity>(this@NewPasswordActivity)
                this@NewPasswordActivity.finish()
            } else {
                bind.progressBar.visibility = View.GONE
                bind.btnResetPassword.visibility = View.VISIBLE
            }
        })

        viewModel.onMessageLiveData.observe(this@NewPasswordActivity, {
            noticeToast(it)
        })

        viewModel.onFailLiveData.observe(this@NewPasswordActivity, {
            noticeToast(it)
        })
    }
}