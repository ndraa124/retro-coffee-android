package com.id124.retrocoffee.activity.customer.forgot_password.email_check

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.forgot_password.new_password.NewPasswordActivity
import com.id124.retrocoffee.activity.customer.login.LoginActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityEmailCheckBinding
import com.id124.retrocoffee.util.form_validate.ValidateAccount

class EmailCheckActivity : BaseActivity<ActivityEmailCheckBinding>(), View.OnClickListener {
    private lateinit var viewViewModel: EmailCheckViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_email_check
        super.onCreate(savedInstanceState)

        initTextWatcher()
        setViewModel()
        subscribeLiveData()
    }

    override fun onClick(v: View?){
        when (v?.id) {
            R.id.btn_cekEmail -> {
                when {
                    !ValidateAccount.valEmail(bind.inputLayoutEmail, bind.inputemails) -> {
                    }
                    else -> {
                        viewViewModel.serviceApi(
                            email = bind.inputemails.text.toString()
                        )
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        intents<LoginActivity>(this@EmailCheckActivity)
    }

    private fun initTextWatcher() {
        bind.btnCekEmail.addTextChangedListener(MyTextWatcher(bind.btnCekEmail))
    }

    private fun setViewModel() {
        viewViewModel = ViewModelProvider(this@EmailCheckActivity).get(EmailCheckViewModel::class.java)
        viewViewModel.setService(createApi(this@EmailCheckActivity))
    }

    private fun subscribeLiveData() {
        viewViewModel.isLoading.observe(this@EmailCheckActivity, {
            if (it) {
                bind.btnCekEmail.visibility = View.GONE
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
                bind.btnCekEmail.visibility = View.VISIBLE
            }
        })

        viewViewModel.onSuccess.observe(this@EmailCheckActivity, {
            val intent = Intent(this@EmailCheckActivity, NewPasswordActivity::class.java)
            intent.putExtra("ac_id", it)
            startActivity(intent)

            this@EmailCheckActivity.finish()
        })

        viewViewModel.onFail.observe(this@EmailCheckActivity, {
            noticeToast(it)
        })
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