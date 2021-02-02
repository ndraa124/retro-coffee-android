package com.id124.retrocoffee.activity.customer.forgot_password.email_check

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.forgot_password.new_password.NewPasswordActivity
<<<<<<< HEAD
=======
import com.id124.retrocoffee.activity.customer.login.LoginActivity
>>>>>>> 298d00a06fabd45d73d76f12f2c047857fd991d0
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityEmailCheckBinding
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valEmail

class EmailCheckActivity : BaseActivity<ActivityEmailCheckBinding>(), View.OnClickListener {
<<<<<<< HEAD
    private lateinit var viewModel: EmailCheckViewModel
=======
    private lateinit var viewViewModel: EmailCheckViewModel

>>>>>>> 298d00a06fabd45d73d76f12f2c047857fd991d0
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
                    !valEmail(bind.inputLayoutEmail, bind.etEmailReset) -> {
                    }
                    else -> {
<<<<<<< HEAD
                        viewModel.serviceApi(bind.etEmailReset.text.toString())
=======
                        viewViewModel.serviceApi(
                            email = bind.inputemails.text.toString()
                        )
>>>>>>> 298d00a06fabd45d73d76f12f2c047857fd991d0
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
<<<<<<< HEAD
                bind.btnCekEmail.visibility = View.VISIBLE
                intents<NewPasswordActivity>(this@EmailCheckActivity)
=======
                bind.progressBar.visibility = View.VISIBLE
>>>>>>> 298d00a06fabd45d73d76f12f2c047857fd991d0
            } else {
                bind.progressBar.visibility = View.GONE
                bind.btnCekEmail.visibility = View.VISIBLE
            }
        })

        viewViewModel.onSuccess.observe(this@EmailCheckActivity, {
            val intent = Intent(this@EmailCheckActivity, NewPasswordActivity::class.java)
            intent.putExtra("ac_id", it)
            startActivity(intent)

<<<<<<< HEAD
    private fun setViewModel() {
        viewModel = ViewModelProvider(this@EmailCheckActivity).get(EmailCheckViewModel::class.java)
        viewModel.setService(createApi(this@EmailCheckActivity))
        viewModel.setSharedPref(sharedPref)
    }

    private fun initTextWatcher() {
        bind.etEmailReset.addTextChangedListener(MyTextWatcher(bind.etEmailReset))
=======
            this@EmailCheckActivity.finish()
        })

        viewViewModel.onFail.observe(this@EmailCheckActivity, {
            noticeToast(it)
        })
>>>>>>> 298d00a06fabd45d73d76f12f2c047857fd991d0
    }

    inner class MyTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.et_email_reset -> valEmail(bind.inputLayoutEmail, bind.etEmailReset)
            }
        }
    }
}