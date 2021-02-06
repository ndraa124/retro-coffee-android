package com.id124.retrocoffee.activity.admin.profile

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.admin.profile.edit_password.EditPasswordAdminActivity
import com.id124.retrocoffee.activity.admin.profile.edit_profile.EditProfileAdminActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityProfileAdminBinding

class ProfileAdminActivity : BaseActivity<ActivityProfileAdminBinding>(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_profile_admin
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setDataSharedPref()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_edit_profile -> {
                intents<EditProfileAdminActivity>(this@ProfileAdminActivity)
            }
            R.id.btn_edit_password -> {
                intents<EditPasswordAdminActivity>(this@ProfileAdminActivity)
            }
            R.id.btn_back -> {
                onBackPressed()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setDataSharedPref()
    }

    override fun onResume() {
        super.onResume()
        setDataSharedPref()
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

    private fun setDataSharedPref() {
        bind.tvName.text = sharedPref.getAcName()
        bind.tvEmail.text = sharedPref.getAcEmail()
        bind.tvPhoneNumber.text = sharedPref.getAcPhone()
        bind.ivImageProfile.setImageResource(R.drawable.profile)
    }
}