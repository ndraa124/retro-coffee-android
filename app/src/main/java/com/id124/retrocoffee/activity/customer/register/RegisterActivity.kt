package com.id124.retrocoffee.activity.customer.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityRegisterBinding
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valCompany
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valEmail
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valName
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valPassConf
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valPassword
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valPhoneNumber
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valPosition

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_register
        super.onCreate(savedInstanceState)


        initTextWatcher()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_create_account -> {
                when {
                    !valName(bind.inputName, bind.etName) -> {
                    }
                    !valEmail(bind.inputLayoutEmail, bind.etEmail) -> {
                    }
                    !valPhoneNumber(bind.inputPhoneNumber, bind.etPhoneNumber) -> {
                    }
                    !valPassword(bind.inputLayoutPassword, bind.etPassword) -> {
                    }
                    !valPassConf(
                        bind.inputLayoutPasswordConfirmation,
                        bind.etPasswordConfirmation,
                        bind.etPassword
                    ) -> {
                    }
                    else -> {
                        noticeToast("Register Succes")
                    }
                }
            }
        }
    }

    private fun initTextWatcher() {
        bind.etName.addTextChangedListener(MyTextWatcher(bind.etName))
        bind.etEmail.addTextChangedListener(MyTextWatcher(bind.etEmail))
        bind.etPhoneNumber.addTextChangedListener(MyTextWatcher(bind.etPhoneNumber))
        bind.etPassword.addTextChangedListener(MyTextWatcher(bind.etPassword))
        bind.etPasswordConfirmation.addTextChangedListener(MyTextWatcher(bind.etPasswordConfirmation))
    }


    inner class MyTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.et_name -> valName(bind.inputName, bind.etName)
                R.id.et_email -> valEmail(bind.inputLayoutEmail, bind.etEmail)
                R.id.et_phone_number -> valPhoneNumber(bind.inputPhoneNumber, bind.etPhoneNumber)
                R.id.et_password -> valPassword(bind.inputLayoutPassword, bind.etPassword)
                R.id.et_password_confirmation -> valPassConf(bind.inputLayoutPasswordConfirmation, bind.etPasswordConfirmation, bind.etPassword)
            }
        }
    }
}