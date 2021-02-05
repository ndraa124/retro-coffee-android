package com.id124.retrocoffee.activity.customer.profile

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.history.HistoryActivity
import com.id124.retrocoffee.activity.customer.profile.adapter.HistoryAdapter
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityProfileBinding
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE

class ProfileActivity : BaseActivity<ActivityProfileBinding>(), View.OnClickListener {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var adapter: HistoryAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_profile
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setHistoryRecyclerView()
        setViewModel()
        subscribeLiveData()
        setDataSharedPref()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_edit_profile -> {
                intents<EditProfileActivity>(this@ProfileActivity)
            }
            R.id.tv_more_history -> {
                intents<HistoryActivity>(this@ProfileActivity)
            }
            R.id.btn_edit_password -> {
                intents<EditPasswordActivity>(this@ProfileActivity)
            }
            R.id.btn_back -> {
                onBackPressed()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.serviceGetCustomer(
            csId = sharedPref.getCsId()
        )

        setDataSharedPref()
    }

    override fun onResume() {
        super.onResume()
        viewModel.serviceGetCustomer(
            csId = sharedPref.getCsId()
        )

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

        if (sharedPref.getCsAddress() == null) {
            bind.tvAddress.text = resources.getString(R.string.empty_address)
            bind.tvAddress.setTextColor(resources.getColor(R.color.gray_500, theme))
        } else {
            bind.tvAddress.text = sharedPref.getCsAddress()
        }
    }

    private fun setHistoryRecyclerView() {
        bind.rvHistory.isNestedScrollingEnabled = true

        layoutManager = LinearLayoutManager(this@ProfileActivity, RecyclerView.HORIZONTAL, false)
        bind.rvHistory.layoutManager = layoutManager

        adapter = HistoryAdapter()
        bind.rvHistory.adapter = adapter
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@ProfileActivity).get(ProfileViewModel::class.java)
        viewModel.setServiceHistory(createApi(this@ProfileActivity))
        viewModel.setServiceCustomer(createApi(this@ProfileActivity))

        viewModel.serviceGetCustomer(
            csId = sharedPref.getCsId()
        )
        viewModel.serviceGetHistory(
            csId = sharedPref.getCsId()
        )
    }

    private fun subscribeLiveData() {
        viewModel.isLoadingHistory.observe(this@ProfileActivity, {
            if (it) {
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
            }
        })

        viewModel.onSuccessHistory.observe(this@ProfileActivity, { list ->
            bind.tvDataNotFound.visibility = View.GONE
            adapter.addList(list)
        })

        viewModel.onSuccessCustomer.observe(this@ProfileActivity, {
            if (it == "" || it == null) {
                bind.ivImageProfile.setImageResource(R.drawable.profile)
            } else {
                sharedPref.createCsPicImage(it)
                bind.imageUrl = BASE_URL_IMAGE + it
            }
        })

        viewModel.onFailHistory.observe(this@ProfileActivity, { message ->
            bind.dataNotFound = message
            bind.tvDataNotFound.visibility = View.VISIBLE
        })
    }
}