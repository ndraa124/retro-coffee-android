package com.id124.retrocoffee.activity.customer.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.history.HistoryActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityProfileBinding
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE

class ProfileActivity : BaseActivity<ActivityProfileBinding>(), View.OnClickListener {
    private lateinit var viewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_profile
        super.onCreate(savedInstanceState)

        setToolbar()
        setViewModel()
        subcsribeLiveData()

        bind.btnEditProfile.setOnClickListener {
            editProfile()
        }
    }

    private fun editProfile() {
        intents<EditProfileActivity>(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.see_more -> {
                intents<HistoryActivity>(this)
            }
        }
    }

    private fun setToolbar() {
        bind.toolbar.title = "My Profile"
        setSupportActionBar(bind.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bind.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setViewModel(){
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.setServiceProfile(createApi(this))
        viewModel.getCustomerById(csId = sharedPref.getCsId())
    }

    private fun subcsribeLiveData(){
        viewModel.isLoading.observe(this) {
            if(it) {
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
            }
        }

        viewModel.listProfile.observe(this) {
            bind.model = it[0]
            bind.imageUrl = BASE_URL_IMAGE + it[0].csImage
        }
    }
}