package com.id124.retrocoffee.activity.customer.forgot_password.email_check

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.forgot_password.new_password.NewPasswordActivity
import com.id124.retrocoffee.activity.customer.login.LoginViewModel
import com.id124.retrocoffee.activity.customer.main.MainActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityEmailCheckBinding
import com.id124.retrocoffee.util.form_validate.ValidateAccount

class EmailCheckActivity : BaseActivity<ActivityEmailCheckBinding>(), View.OnClickListener {
    private lateinit var viewModel: EmailCheckModel
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_email_check
        super.onCreate(savedInstanceState)

        initTextWatcher()
        setViewModel()
        subscribeLiveData()
    }
    override fun onClick(v: View?){
        when (v?.id) {
            R.id.btn_login -> {
                when {
                    !ValidateAccount.valEmail(bind.inputLayoutEmail, bind.inputemails) -> {
                    }
                    else -> {
                        viewModel.toString()
                    }
                }
            }
        }
    }
    private fun subscribeLiveData() {
        viewModel.isLoadingLiveData.observe(this@EmailCheckActivity, {
            bind.btnCekEmail.visibility = View.GONE
            bind.btnCekEmail.visibility = View.VISIBLE
        })

        viewModel.onSuccessLiveData.observe(this@EmailCheckActivity, {
            if (it) {
                bind.btnCekEmail.visibility = View.GONE
                bind.btnCekEmail.visibility = View.VISIBLE
                intents<NewPasswordActivity>(this@EmailCheckActivity)
                this@EmailCheckActivity.finish()
            } else {
                bind.btnCekEmail.visibility = View.GONE
                bind.btnCekEmail.visibility = View.VISIBLE
            }
        })

        viewModel.onFailLiveData.observe(this@EmailCheckActivity, {
            noticeToast(it)
        })
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@EmailCheckActivity).get(EmailCheckModel::class.java)
        viewModel.setService(createApi(this@EmailCheckActivity))
        viewModel.setSharedPref(sharedPref)
    }

    private fun initTextWatcher() {
        bind.btnCekEmail.addTextChangedListener(MyTextWatcher(bind.btnCekEmail))
    }

    inner class MyTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.inputemails -> ValidateAccount.valEmail(bind.inputLayoutEmail, bind.inputemails)
            }
        }
    }
}