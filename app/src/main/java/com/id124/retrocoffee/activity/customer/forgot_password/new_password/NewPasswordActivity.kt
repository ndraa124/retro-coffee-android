package com.id124.retrocoffee.activity.customer.forgot_password.new_password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.login.LoginActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityNewPasswordBinding
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valPassConf
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valPassword

class NewPasswordActivity : BaseActivity<ActivityNewPasswordBinding>(), View.OnClickListener {
    private lateinit var viewModel: NewPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_new_password
        super.onCreate(savedInstanceState)

        initTextWatcher()
        setViewModel()
        subscribeLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_reset_password -> {
                when {
                    !valPassword(bind.inputLayoutPassword, bind.etPassword) -> {}
                    !valPassConf(bind.inputLayoutPasswordConfirmation, bind.etPasswordConfirmation, bind.etPassword) -> {}
                    else -> {
                        viewModel.serviceResetPassword(
                            acId = intent.getIntExtra("ac_id", 0),
                            acPassword = bind.etPassword.text.toString()
                        )
                    }
                }
            }
        }
    }

    private fun initTextWatcher() {
        bind.etPassword.addTextChangedListener(MyTextWatcher(bind.etPassword))
        bind.etPasswordConfirmation.addTextChangedListener(MyTextWatcher(bind.etPasswordConfirmation))
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@NewPasswordActivity).get(NewPasswordViewModel::class.java)
        viewModel.setService(createApi(this@NewPasswordActivity))
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this@NewPasswordActivity, {
            if (it) {
                bind.btnResetPassword.visibility = View.GONE
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
                bind.btnResetPassword.visibility = View.VISIBLE
            }
        })

        viewModel.onSuccess.observe(this@NewPasswordActivity, {
            if (it) {
                noticeToast("Success to reset password")

                intents<LoginActivity>(this@NewPasswordActivity)
                finish()
            }
        })

        viewModel.onFail.observe(this@NewPasswordActivity, {
            noticeToast(it)
        })
    }

    inner class MyTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.et_password -> valPassword(bind.inputLayoutPassword, bind.etPassword)
                R.id.et_password_confirmation -> valPassConf(bind.inputLayoutPasswordConfirmation, bind.etPasswordConfirmation, bind.etPassword)
            }
        }
    }
}