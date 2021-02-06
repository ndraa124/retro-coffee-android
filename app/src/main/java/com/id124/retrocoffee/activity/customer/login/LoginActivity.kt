package com.id124.retrocoffee.activity.customer.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.forgot_password.email_check.EmailCheckActivity
import com.id124.retrocoffee.activity.customer.main.MainActivity
import com.id124.retrocoffee.activity.customer.welcome.WelcomeActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityLoginBinding
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valEmail
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valPassword

class LoginActivity : BaseActivity<ActivityLoginBinding>(), View.OnClickListener {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_login
        super.onCreate(savedInstanceState)

        setStatusBar()
        initTextWatcher()
        setViewModel()
        subscribeLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_forget_password -> {
                intents<EmailCheckActivity>(this)
            }
            R.id.btn_login -> {
                when {
                    !valEmail(bind.inputLayoutEmail, bind.etEmail) -> {
                    }
                    !valPassword(bind.inputLayoutPassword, bind.etPassword) -> {
                    }
                    else -> {
                        viewModel.serviceApi(
                            email = bind.etEmail.text.toString(),
                            password = bind.etPassword.text.toString()
                        )
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        intents<WelcomeActivity>(this@LoginActivity)
        this@LoginActivity.finish()
    }

    private fun setStatusBar() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = this.resources.getColor(R.color.background, theme)
    }

    private fun subscribeLiveData() {
        viewModel.isLoadingLiveData.observe(this@LoginActivity) {
            bind.btnLogin.visibility = View.GONE
            bind.progressBar.visibility = View.VISIBLE
        }

        viewModel.onSuccessLiveData.observe(this@LoginActivity) {
            if (it) {
                bind.progressBar.visibility = View.GONE
                bind.btnLogin.visibility = View.VISIBLE

                intents<MainActivity>(this@LoginActivity)
                this@LoginActivity.finish()
            } else {
                bind.progressBar.visibility = View.GONE
                bind.btnLogin.visibility = View.VISIBLE
            }
        }

        viewModel.onFailLiveData.observe(this@LoginActivity, {
            noticeToast(it)
        })
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@LoginActivity).get(LoginViewModel::class.java)
        viewModel.setService(createApi(this@LoginActivity))
        viewModel.setSharedPref(sharedPref)
    }

    private fun initTextWatcher() {
        bind.etEmail.addTextChangedListener(MyTextWatcher(bind.etEmail))
        bind.etPassword.addTextChangedListener(MyTextWatcher(bind.etPassword))
    }

    inner class MyTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.et_email -> valEmail(bind.inputLayoutEmail, bind.etEmail)
                R.id.et_password -> valPassword(bind.inputLayoutPassword, bind.etPassword)
            }
        }
    }

}