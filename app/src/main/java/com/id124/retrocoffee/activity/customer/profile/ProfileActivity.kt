package com.id124.retrocoffee.activity.customer.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityProfileBinding

class ProfileActivity : BaseActivity<ActivityProfileBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_profile
        super.onCreate(savedInstanceState)

        bind.toolbar.title = "My Profile"
        setSupportActionBar(bind.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bind.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        bind.btnEditProfile.setOnClickListener {
            editProfile()
        }
    }

    private fun editProfile() {
        intents<EditProfileActivity>(this)
    }
}