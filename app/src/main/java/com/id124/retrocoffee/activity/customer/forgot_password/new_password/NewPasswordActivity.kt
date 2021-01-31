package com.id124.retrocoffee.activity.customer.forgot_password.new_password

import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.profile.EditProfileViewModel
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityNewPasswordBinding

class NewPasswordActivity : BaseActivity<ActivityNewPasswordBinding>() {
    private lateinit var viewModel: PasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_new_password
        super.onCreate(savedInstanceState)

        bind.resetPassword.setOnClickListener{
            val acId = sharedPref.getAcEmail()
            val password = bind.etEmail.text.toString()

            viewModel.ResetPassword(acId, password)
        }
    }
}